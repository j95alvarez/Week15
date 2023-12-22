package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private CustomerDao customerDao;

	/*
	 * Pet Store Section
	 */
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		// TODO Auto-generated method stub
		PetStore petStore = findOrCreatePetStore(petStoreData.getPetStoreId());
		copyPetStoreFields(petStore, petStoreData);
		
		return new PetStoreData(petStoreDao.save(petStore));
	}
	
	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		// TODO Auto-generated method stub
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		// TODO Auto-generated method stub
		
		if (petStoreId == null)
			return new PetStore();
		else
			return petStoreDao.findById(petStoreId).orElseThrow(
					()-> new NoSuchElementException("Pet Store with ID = " + petStoreId + " not found."));
	}

	/*
	 * Employee Section
	 */
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		// TODO Auto-generated method stub
		PetStore petStore = findOrCreatePetStore(petStoreId);
		Long employeeId = petStoreEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		
		copyEmployeeFields(employee, petStoreEmployee);
		employee.setPetStore(petStore);
		
		petStore.getEmployees().add(employee);
		
		return new PetStoreEmployee(employeeDao.save(employee));
	}

	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		// TODO Auto-generated method stub
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		
	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		// TODO Auto-generated method stub
		if (employeeId == null)
			return new Employee();
		else
			return findEmployeeById(petStoreId, employeeId);
	}

	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		// TODO Auto-generated method stub
		Employee employee = employeeDao.findById(employeeId).orElseThrow(
				() -> new NoSuchElementException("Employee ID= " + employeeId + " was not found!"));
		
		if (employee.getPetStore().getPetStoreId().equals(petStoreId))
			return employee;
		else
			throw new IllegalArgumentException("Employee ID= " + employeeId + " is not in Pet Store ID= " + petStoreId);
	}

	/*
	 * Customer Section
	 */
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		// TODO Auto-generated method stub
		PetStore petStore = findOrCreatePetStore(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
		
		copyCustomerFields(customer, petStoreCustomer);
		customer.getPetStores().add(petStore);
		
		petStore.getCustomers().add(customer);
		
		return new PetStoreCustomer(customerDao.save(customer));

	}

	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		// TODO Auto-generated method stub
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		
	}

	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		// TODO Auto-generated method stub
		if (customerId == null)
			return new Customer();
		else
			return findCustomerById(petStoreId, customerId);
	}

	private Customer findCustomerById(Long petStoreId, Long customerId) {
		// TODO Auto-generated method stub
		Customer customer = customerDao.findById(customerId).orElseThrow(
				() -> new NoSuchElementException("Customer ID= " + customerId + " was not found!"));

		for (PetStore petStore : customer.getPetStores())
			if (petStore.getPetStoreId().equals(petStoreId))
				return customer;
		
		throw new IllegalArgumentException("Customer ID = " + customerId + " is not in Pet Store ID= " +petStoreId);
	}

	/*
	 * List all Pet Stores
	 */
	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		// TODO Auto-generated method stub
		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> result = new LinkedList<>();
		
		for (PetStore petStore : petStores) {
			PetStoreData psd = new PetStoreData(petStore);
			
			psd.getCustomers().clear();
			psd.getEmployees().clear();
			
			result.add(psd);
		}
		return result;
	}

	/*
	 * Get Pet Store by ID
	 */
	@Transactional(readOnly = true)
	public PetStoreData getPetStoreById(Long petStoreId) {
		// TODO Auto-generated method stub
		PetStoreData psd = new PetStoreData();
		
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		if (petStore != null) {
			psd.setPetStoreId(petStore.getPetStoreId());
			psd.setPetStoreName(petStore.getPetStoreName());
			psd.setPetStoreAddress(petStore.getPetStoreAddress());
			psd.setPetStoreCity(petStore.getPetStoreCity());
			psd.setPetStoreState(petStore.getPetStoreState());
			psd.setPetStoreZip(petStore.getPetStoreZip());
			psd.setPetStorePhone(petStore.getPetStorePhone());
		}
		
		return psd;
	}

	/*
	 * Delete Pet Store by ID
	 */
	@Transactional(readOnly = false)
	public void deletePetStoreById(Long petStoreId) {
		// TODO Auto-generated method stub
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		petStoreDao.delete(petStore);
	}
}
