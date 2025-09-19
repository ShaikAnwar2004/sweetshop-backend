package com.example.demo.controller;

import com.example.demo.model.Sweet;
import com.example.demo.repository.SweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/sweets")
public class SweetController {

    @Autowired
    private SweetRepository sweetRepo;

    // ✅ Add a new sweet (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Sweet> addSweet(@RequestBody Sweet sweet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sweetRepo.save(sweet));
    }

    // ✅ View all sweets (Public)
    @GetMapping("/all")
    public ResponseEntity<List<Sweet>> getAllSweets() {
        return ResponseEntity.ok(sweetRepo.findAll());
    }

    // ✅ View inventory (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/inventory")
    public ResponseEntity<List<Sweet>> getInventory() {
        return ResponseEntity.ok(sweetRepo.findAll());
    }

    // ✅ Search sweets by name, category, or price range (Public)
    @GetMapping("/search")
    public ResponseEntity<List<Sweet>> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        List<Sweet> results = sweetRepo.findAll();

        if (name != null) results.removeIf(s -> !s.getName().toLowerCase().contains(name.toLowerCase()));
        if (category != null) results.removeIf(s -> !s.getCategory().equalsIgnoreCase(category));
        if (minPrice != null) results.removeIf(s -> s.getPrice() < minPrice);
        if (maxPrice != null) results.removeIf(s -> s.getPrice() > maxPrice);

        return ResponseEntity.ok(results);
    }

    // ✅ Update sweet details (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Sweet> updateSweet(@PathVariable Long id, @RequestBody Sweet updated) {
        Sweet sweet = sweetRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sweet not found"));

        sweet.setName(updated.getName());
        sweet.setCategory(updated.getCategory());
        sweet.setPrice(updated.getPrice());
        sweet.setQuantity(updated.getQuantity());

        return ResponseEntity.ok(sweetRepo.save(sweet));
    }

    // ✅ Delete sweet (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSweet(@PathVariable Long id) {
        if (!sweetRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Sweet not found");
        }
        sweetRepo.deleteById(id);
        return ResponseEntity.ok("✅ Sweet deleted");
    }

    // ✅ Purchase sweet (User or Admin)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{id}/purchase")
    public ResponseEntity<String> purchaseSweet(@PathVariable Long id) {
        Sweet sweet = sweetRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sweet not found"));

        if (sweet.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("❌ Out of stock");
        }

        sweet.setQuantity(sweet.getQuantity() - 1);
        sweetRepo.save(sweet);
        return ResponseEntity.ok("✅ Sweet purchased");
    }

    // ✅ Restock sweet (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/restock")
    public ResponseEntity<String> restockSweet(@PathVariable Long id) {
        Sweet sweet = sweetRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sweet not found"));

        sweet.setQuantity(sweet.getQuantity() + 10); // Customize as needed
        sweetRepo.save(sweet);
        return ResponseEntity.ok("✅ Sweet restocked");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getReport() {
        List<Sweet> sweets = sweetRepo.findAll();

        int totalStock = sweets.stream().mapToInt(Sweet::getQuantity).sum();
        double totalValue = sweets.stream().mapToDouble(s -> s.getPrice() * s.getQuantity()).sum();
        Sweet topSweet = sweets.stream()
            .max(Comparator.comparingInt(Sweet::getQuantity))
            .orElse(null);

        Map<String, Object> report = new HashMap<>();
        report.put("totalStock", totalStock);
        report.put("totalValue", totalValue);
        report.put("topSweet", topSweet != null ? topSweet.getName() : "N/A");

        return ResponseEntity.ok(report);
    }
}