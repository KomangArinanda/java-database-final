package com.project.code.Controller;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StoreRepository storeRepository;

    @PostMapping
    public Map<String, Long> addStore(@RequestBody Store store) {
        return Map.of("message", storeRepository.save(store)
            .getId());
    }

    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO) {
        try {
            orderService.saveOrder(placeOrderRequestDTO);
            return Map.of("message", "Order placed successfully");
        } catch (Exception e) {
            return Map.of("Error", e.getMessage());
        }
    }

    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId) {
        return storeRepository.existsById(storeId);
    }

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to designate it as a REST controller for handling HTTP requests.
//    - Map the class to the `/store` URL using `@RequestMapping("/store")`.


    // 2. Autowired Dependencies:
//    - Inject the following dependencies via `@Autowired`:
//        - `StoreRepository` for managing store data.
//        - `OrderService` for handling order-related functionality.


    // 3. Define the `addStore` Method:
//    - Annotate with `@PostMapping` to create an endpoint for adding a new store.
//    - Accept `Store` object in the request body.
//    - Return a success message in a `Map<String, String>` with the key `message` containing store creation confirmation.


    // 4. Define the `validateStore` Method:
//    - Annotate with `@GetMapping("validate/{storeId}")` to check if a store exists by its `storeId`.
//    - Return a **boolean** indicating if the store exists.


    // 5. Define the `placeOrder` Method:
//    - Annotate with `@PostMapping("/placeOrder")` to handle order placement.
//    - Accept `PlaceOrderRequestDTO` in the request body.
//    - Return a success message with key `message` if the order is successfully placed.
//    - Return an error message with key `Error` if there is an issue processing the order.


}
