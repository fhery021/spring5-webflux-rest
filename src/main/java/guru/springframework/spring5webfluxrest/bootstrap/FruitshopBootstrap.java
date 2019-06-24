package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class FruitshopBootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public FruitshopBootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("loading data...");
        if(categoryRepository.count().block() == 0){
            loadCategories();
        }
        if (vendorRepository.count().block() == 0){
            loadVendors();
        }
        System.out.println("done loading data.");
        System.out.println("Categories count: " + categoryRepository.count().block());
        System.out.println("Vendors count: " + vendorRepository.count().block());
    }

    private void loadVendors() {
        Vendor vendor = new Vendor();
        vendor.setFirstName("Lajos");
        vendor.setLastName("Aprilis");
        vendorRepository
                .save(vendor)
                .block();
    }

    private void loadCategories() {
        Category category = new Category();
        category.setDescription("apple");
        categoryRepository
                .save(category)
                .block();
    }

}
