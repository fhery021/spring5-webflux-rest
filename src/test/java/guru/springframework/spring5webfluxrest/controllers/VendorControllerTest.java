package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static guru.springframework.spring5webfluxrest.controllers.VendorController.VENDORS_BASE_URI;
import static org.mockito.ArgumentMatchers.any;

public class VendorControllerTest {

    private WebTestClient webTestClient;
    private VendorRepository vendorRepository;
    private VendorController controller;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        controller = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    public void list() {

        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Fred").lastName("Flintstone").build(),
                        Vendor.builder().firstName("Barney").lastName("Rubble").build()));

        webTestClient.get()
                .uri(VENDORS_BASE_URI)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        BDDMockito.given(vendorRepository.findById("someid"))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").lastName("Johns").build()));

        webTestClient.get()
                .uri(VENDORS_BASE_URI+ "/someid")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void createVendors(){

        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(
                        Vendor.builder().firstName("1").build(),
                        Vendor.builder().firstName("2").build()));
        Mono<Vendor> returnedMono = Mono.just(Vendor.builder().build());

        webTestClient.post()
                .uri(VENDORS_BASE_URI)
                .body(returnedMono, Vendor.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void updateVendor(){
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(
                        Vendor.builder().firstName("James").build()));

        Mono<Vendor> input = Mono.just(Vendor.builder().build());

        webTestClient.put()
                .uri(VENDORS_BASE_URI + "/123")
                .body(input, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.firstName", equals("James"));

    }
}