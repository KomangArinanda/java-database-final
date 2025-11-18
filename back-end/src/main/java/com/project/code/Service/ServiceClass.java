package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceClass {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public Inventory getInventoryId(Inventory inventory) {
        return inventoryRepository.findByProductIdandStoreId(inventory.getProduct()
            .getId(), inventory.getStore()
            .getId());
    }

    public boolean validateInventory(Inventory inventory) {
        return Optional.ofNullable(inventoryRepository.findByProductIdandStoreId(inventory.getProduct()
                .getId(), inventory.getStore()
                .getId()))
            .isEmpty();
    }

    public boolean validateProductId(long id) {
        return productRepository.existsById(id);
    }

// 1. **validateInventory Method**:
//    - Checks if an inventory record exists for a given product and store combination.
//    - Parameters: `Inventory inventory`
//    - Return Type: `boolean` (Returns `false` if inventory exists, otherwise `true`)

// 2. **validateProduct Method**:
//    - Checks if a product exists by its name.
//    - Parameters: `Product product`
//    - Return Type: `boolean` (Returns `false` if a product with the same name exists, otherwise `true`)

// 3. **ValidateProductId Method**:
//    - Checks if a product exists by its ID.
//    - Parameters: `long id`
//    - Return Type: `boolean` (Returns `false` if the product does not exist with the given ID, otherwise `true`)

// 4. **getInventoryId Method**:
//    - Fetches the inventory record for a given product and store combination.
//    - Parameters: `Inventory inventory`
//    - Return Type: `Inventory` (Returns the inventory record for the product-store combination)

}
