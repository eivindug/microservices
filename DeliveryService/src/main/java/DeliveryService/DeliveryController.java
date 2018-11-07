package DeliveryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DeliveryController implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryController.class);

	List<DeliveryBean> deliveryBeans = new ArrayList<DeliveryBean>();
	private static int orderIds = 0;

	@Autowired
	private WebClient.Builder webClientBuilder;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        LOGGER.info("DeliveryController: init");

         //Subscribe to stream
        //Flux<String> messages = webClientBuilder.build().get().uri("http://delivery-service/delivery").retrieve().bodyToFlux(String.class);
        //messages.subscribe(message -> LOGGER.info("subscriber: " + message));
    }

    public synchronized String incrementOrderId(){
    	return ""+orderIds++;
	}

    @PostMapping(value="/createOrder", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> createOrder(@RequestBody ShoppingCartServiceBean service1Bean) {
    	DeliveryBean db = new DeliveryBean();
    	db.setId(incrementOrderId());
    	db.setStatus("createdOrder");
    	db.setShoppingcartBean(service1Bean);
    	deliveryBeans.add(db);

		LOGGER.info("createOrder" + db);

    	return Mono.just(db.getId());
	}

//	@GetMapping()
//	public Flux<DeliveryService.DeliveryBean> getAll() {
//		LOGGER.info("getAll");
//		waitFor2Seconds();
//
//		LOGGER.info("returning flux");
//		return Flux.just(new DeliveryService.DeliveryBean("1"), new DeliveryService.DeliveryBean("2"));
//	}
	@GetMapping(value = "/getStatus/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	Mono<DeliveryBean> getOrder(@RequestParam("orderId") int id){
    	LOGGER.info("Get order " + id);
    	DeliveryBean ret = null;
    	for (DeliveryBean dl : deliveryBeans){
    		if (dl.getId().equals(""+id) ){
    			return Mono.just(dl);
			}
		}
		return Mono.empty();
	}

	private Mono<Void> updateStatus(String orderId, String status){
    	for (int i = 0; i < deliveryBeans.size();i++){
    		if (deliveryBeans.get(i).equals(""+orderId)){
    			deliveryBeans.get(i).setStatus(status);
			}
		}
		return Mono.empty();
	}

	@PostMapping(value = "/deliverOrder/")
	Mono<Void> deliverOrder(@RequestBody ShoppingCartServiceBean shoppingCartServiceBean) {
    	String x = "";
    	for (DeliveryBean dl : deliveryBeans){
    		if (dl.getShoppingCartBean().getId() == shoppingCartServiceBean.getId()){
    			x = dl.getId();
			}
		}
		if (x.equals("")){
    		return Mono.empty();
		}


		LOGGER.info("create: id={}", x);
		updateStatus(x, "pending");
		waitFor2Seconds();


		return Mono.empty();
	}

	private void waitFor2Seconds() {
		LOGGER.info("sleeping for 2 seconds");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}