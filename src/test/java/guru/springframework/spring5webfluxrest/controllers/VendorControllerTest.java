package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void listAllVendors(){
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(
                Flux.just(Vendor.builder().firstName("David").lastName("Hasselhoff").build(),
                        Vendor.builder().firstName("James").lastName("Bond").build()));
        webTestClient.get().uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorById(){
        Vendor vendor = Vendor.builder().firstName("John").lastName("Wick").build();
        BDDMockito.given(vendorRepository.findById("123"))
                .willReturn(
                Mono.just(vendor));
        webTestClient.get().uri("/api/v1/vendors/123")
                .exchange()
                .expectBody(Vendor.class)
                .isEqualTo(vendor);
    }
}