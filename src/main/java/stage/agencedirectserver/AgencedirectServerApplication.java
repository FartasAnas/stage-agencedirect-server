package stage.agencedirectserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.entities.Pack;
import stage.agencedirectserver.entities.Role;
import stage.agencedirectserver.services.AgenceService;
import stage.agencedirectserver.services.AgentService;
import stage.agencedirectserver.services.PackService;
import stage.agencedirectserver.services.RoleService;

import java.util.ArrayList;

@SpringBootApplication
public class AgencedirectServerApplication {

	public static void main(String[] args) {
			SpringApplication.run(AgencedirectServerApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(AgentService agentService, RoleService roleService, AgenceService agenceService , PackService packService){
		return args -> {
			try {
				roleService.addRole(new Role(null,"ROLE_AGENT"));
				roleService.addRole(new Role(null,"ROLE_ADMIN"));
				roleService.addRole(new Role(null,"ROLE_CLIENT"));

				agenceService.addAgence(new Agence(null,"agence direct","690","online","online","4.51184189","5.949496",new ArrayList<>()));

				agentService.addAgent(new Agent(null,"fartas","anas","fartasanas","1234",null,new ArrayList<>()));


				agentService.addRoleToAgent("fartasanas","ROLE_ADMIN");

				packService.addPack(new Pack(null,"pack jeune","0 Dhs/mois",null));
				packService.addPack(new Pack(null,"pack azur","0 Dhs pour les Jeunes Salariés 18-39 ans*",null));
				packService.addPack(new Pack(null,"pack gold","0 Dhs les 6 premiers mois, puis 48 Dhs HT/mois",null));
				packService.addPack(new Pack(null,"pack platinum","0 Dhs les 6 premiers mois, puis 89 Dhs HT/mois",null));

			}
			catch (Exception e){
				System.out.println("Duplicated Default Values");
			}

		};
	}

}
