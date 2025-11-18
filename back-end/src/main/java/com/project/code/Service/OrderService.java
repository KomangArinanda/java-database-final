package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Transactional
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequestDTO) {
        Customer customer = customerRepository.findByEmail(placeOrderRequestDTO.getCustomerEmail());
        if (customer == null) {
            Customer newCustomer = new Customer(placeOrderRequestDTO.getCustomerEmail(),
                placeOrderRequestDTO.getCustomerName(), placeOrderRequestDTO.getCustomerPhone());
            customer = customerRepository.save(newCustomer);
        }

        Store store = storeRepository.findById(placeOrderRequestDTO.getStoreId())
            .orElseThrow(() -> new RuntimeException("Store not found"));

        OrderDetails orderDetails = new OrderDetails(customer, store, placeOrderRequestDTO.getTotalPrice(),
            LocalDateTime.now());
        orderDetails = orderDetailsRepository.save(orderDetails);

        for (PurchaseProductDTO productDTO : placeOrderRequestDTO.getPurchaseProduct()) {
            Product product = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

            Inventory inventory = inventoryRepository.findByProductIdandStoreId(productDTO.getId(), store.getId());
            inventory.setStockLevel(inventory.getStockLevel() - productDTO.getQuantity());
            inventoryRepository.save(inventory);

            OrderItem orderItem = new OrderItem(orderDetails, product, productDTO.getQuantity(), product.getPrice());
            orderItemRepository.save(orderItem);
        }
    }
// 1. **saveOrder Method**:
//    - Processes a customer's order, including saving the order details and associated items.
//    - Parameters: `PlaceOrderRequestDTO placeOrderRequest` (Request data for placing an order)
//    - Return Type: `void` (This method doesn't return anything, it just processes the order)

// 2. **Retrieve or Create the Customer**:
//    - Check if the customer exists by their email using `findByEmail`.
//    - If the customer exists, use the existing customer; otherwise, create and save a new customer using `customerRepository.save()`.

// 3. **Retrieve the Store**:
//    - Fetch the store by ID from `storeRepository`.
//    - If the store doesn't exist, throw an exception. Use `storeRepository.findById()`.

// 4. **Create OrderDetails**:
//    - Create a new `OrderDetails` object and set customer, store, total price, and the current timestamp.
//    - Set the order date using `java.time.LocalDateTime.now()` and save the order with `orderDetailsRepository.save()`.

// 5. **Create and Save OrderItems**:
//    - For each product purchased, find the corresponding inventory, update stock levels, and save the changes using `inventoryRepository.save()`.
//    - Create and save `OrderItem` for each product and associate it with the `OrderDetails` using `orderItemRepository.save()`.


}
