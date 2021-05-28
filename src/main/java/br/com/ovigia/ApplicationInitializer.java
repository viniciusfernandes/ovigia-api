package br.com.ovigia;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.repository.VigiaRepository;
import br.com.ovigia.route.ClienteRoutesBuilder;
import br.com.ovigia.route.VigiaRoutesBuilder;
import br.com.ovigia.service.ClienteService;
import br.com.ovigia.service.VigiaService;

public class ApplicationInitializer implements ApplicationContextInitializer<GenericApplicationContext> {

	@Override
	public void initialize(GenericApplicationContext context) {
		MongoRegister.registry(context);
		RepositoryRegister.registry(context);
		ServiceRegister.registry(context);
		RoutesRegister.registry(context);
	}
}

class MongoRegister {

	static void registry(GenericApplicationContext context) {
		MongoDatabase db = MongoClients.create("mongodb://vinicius:teste@localhost:27017").getDatabase("ovigia");
		context.registerBean(MongoDatabase.class, () -> db);
	}
}

class RepositoryRegister {

	static void registry(GenericApplicationContext context) {
		MongoDatabase db = context.getBean(MongoDatabase.class);
		context.registerBean(VigiaRepository.class, () -> new VigiaRepository(db));
	}
}

class ServiceRegister {
	static void registry(GenericApplicationContext context) {
		context.registerBean(VigiaService.class, () -> new VigiaService(context.getBean(VigiaRepository.class)));
	}
}

class RoutesRegister {
	static void registry(GenericApplicationContext context) {
		final var router = new VigiaRoutesBuilder(context.getBean(VigiaService.class)).build()
				.and(new ClienteRoutesBuilder(context.getBean(ClienteService.class)).build());

		context.registerBean(RouterFunction.class, () -> router);
	}
}