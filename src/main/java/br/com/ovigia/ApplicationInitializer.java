package br.com.ovigia;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.businessrule.CriarVigiaRule;
import br.com.ovigia.repository.VigiaRepository;

public class ApplicationInitializer implements ApplicationContextInitializer<GenericApplicationContext> {

	@Override
	public void initialize(GenericApplicationContext context) {
		MongoRegister.registry(context);
		RepositoryRegister.registry(context);
		ServiceRegister.registry(context);
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
		context.registerBean(CriarVigiaRule.class, () -> new CriarVigiaRule(context.getBean(VigiaRepository.class)));
	}
}
