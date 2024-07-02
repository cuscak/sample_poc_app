package andrej.cuscak.dms;

import andrej.cuscak.dms.model.*;
import andrej.cuscak.dms.repository.DocumentRepository;
import andrej.cuscak.dms.repository.FolderRepository;
import andrej.cuscak.dms.repository.OwnerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.time.LocalDateTime;

@SpringBootApplication
public class DmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmsApplication.class, args);
	}

	//@Bean
	CommandLineRunner commandLineRunner(
			DocumentRepository documentRepository,
			OwnerRepository ownerRepository,
			FolderRepository folderRepository
	){
		return args -> {
			Owner john_entity = ownerRepository.save(
					new Owner(null, "user1", "John", "Doe", "john@example.com")
			);

			AggregateReference<Owner, Long> john = AggregateReference.to(john_entity.id());

			AggregateReference<Owner, Long> jane = AggregateReference.to(
					ownerRepository.save(
							new Owner(null, "user2", "Jane", "Smith", "jane@example.com")
					).id()
			);

			Owner alice_entity = ownerRepository.save(
					new Owner(null, "user3", "Alice", "Johnson", "alice@example.com")
			);
			AggregateReference<Owner, Long> alice = AggregateReference.to(alice_entity.id());


			AggregateReference<Folder, Long> root = AggregateReference.to(
					folderRepository.save(
							new Folder(null, "root", null)
					).id()
			);

			AggregateReference<Folder, Long> home = AggregateReference.to(
					folderRepository.save(
							new Folder(null, "root/home", root)
					).id()
			);

			folderRepository.save(
					new Folder(null, "root/tmp", root)
			);

			DocumentMetaData meta = new DocumentMetaData(
					LocalDateTime.now(),
					null,
					"This is the description for Sample Document 1.",
					150,
					DocumentTypes.TEXT
			);

			documentRepository.save(
					new Document(null,
							"Sample Document 1",
							meta,
							john,
							root)
			);
			documentRepository.save(
					new Document(null,
							"Sample Document 2",
							meta,
							john,
							root)
			);
			Document doc3 = documentRepository.save(
					new Document(null,
							"Sample Document 3",
							meta,
							alice,
							home)
			);

//			documentRepository.delete(doc3);
//			ownerRepository.delete(alice_entity);
		};
	}

}
