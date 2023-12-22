package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	@Autowired
	private PetStoreService petStoreService;
	
	
	
	/*
	 * Pet Store Section
	 */
	@PostMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData createPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Creating Pet Store ID= " + petStoreData);
		
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PutMapping("/pet_store/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating Pet Store ID= " + petStoreData);
		
		return petStoreService.savePetStore(petStoreData);
		
	}
	
	
	/*
	 * Employee section
	 */
	@PostMapping("pet_store/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee addEmployee(@PathVariable Long petStoreId, @RequestBody PetStoreEmployee petStoreEmployee) {
		log.info("Creating Employee to Pet Store ID= "+ petStoreId + " Employee ID= " + petStoreEmployee);
		
		return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
	}
	
	/*
	 * Customer section
	 */
	@PostMapping("pet_store/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer addCustomer(@PathVariable Long petStoreId, @RequestBody PetStoreCustomer petStoreCustomer) {
		log.info("Creating Customer to Pet Store " + petStoreId + " , Customer " + petStoreCustomer);
		
		return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
	}
	
	/*
	 * Retrieve all Pet Store
	 */
	@GetMapping("/pet_store")
	public List<PetStoreData> retrieveAllPetStore() {
		log.info("Retreiving all Pet Stores");
		
		return petStoreService.retrieveAllPetStores();
	}
	
	/*
	 * Retreive Pet Store by ID
	 */
	@GetMapping("pet_store/{petStoreId}")
	public PetStoreData getPetStoreById(@PathVariable Long petStoreId) {
		log.info("Retreiving Pet Store ID= " + petStoreId);
		
		return petStoreService.getPetStoreById(petStoreId);
	}
	
	/*
	 * Delete Pet Store section
	 */
	@DeleteMapping("pet_store/{petStoreId}")
	public Map<String, String> deletePetStoreId(@PathVariable Long petStoreId) {
		log.info("Deleting Pet Store ID=" + petStoreId);
		
		petStoreService.deletePetStoreById(petStoreId);
		
		return Map.of("message", "Success in deleting Pet Store ID= " + petStoreId);
		
	}
}
