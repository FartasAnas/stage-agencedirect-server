package stage.agencedirectserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.entities.Role;
import stage.agencedirectserver.services.AgenceService;
import stage.agencedirectserver.services.AgentService;
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
	CommandLineRunner run(AgentService agentService, RoleService roleService, AgenceService agenceService){
		return args -> {
			try {
				roleService.addRole(new Role(null,"ROLE_DIRECTOR"));
				roleService.addRole(new Role(null,"ROLE_AGENT"));
				roleService.addRole(new Role(null,"ROLE_ADMIN"));

				agenceService.addAgence(new Agence(null,"Agence baraka","Casa","Galina Rue 22 N 208","4.51184189","5.949496",new ArrayList<>()));

				agentService.addAgent(new Agent(null,"fartas","anas","fartasanas","1234",null,new ArrayList<>()));


				agentService.addRoleToAgent("fartasanas","ROLE_ADMIN");
			}
			catch (Exception e){
				System.out.println("Duplicated Default Values");
			}

		};
	}

}
