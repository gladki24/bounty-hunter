package com.developers.bountyhunter.resource.contract;

import com.developers.bountyhunter.dto.contract.ContractDTO;
import com.developers.bountyhunter.dto.contract.ContractFormDTO;
import com.developers.bountyhunter.mapper.contract.ContractMapper;
import com.developers.bountyhunter.model.contract.Contract;
import com.developers.bountyhunter.model.person.UserAccount;
import com.developers.bountyhunter.model.person.Victim;
import com.developers.bountyhunter.model.world.District;
import com.developers.bountyhunter.service.contract.ContractService;
import com.developers.bountyhunter.service.person.UserAccountService;
import com.developers.bountyhunter.service.person.VictimService;
import com.developers.bountyhunter.service.review.ReviewService;
import com.developers.bountyhunter.service.world.DistrictService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractResource {

	private final ContractService contractService;
	private final DistrictService districtService;
	private final VictimService victimService;
	private final UserAccountService userAccountService;

	private ContractMapper contractMapper = Mappers.getMapper(ContractMapper.class);

	@GetMapping("/{id}")
	private ResponseEntity<ContractDTO> getById(@PathVariable("id") Long id) {

		Optional<Contract> contract = contractService.findById(id);

		return contract.map(value -> new ResponseEntity<>(contractMapper.contractToContractDTO(value), HttpStatus.OK)).
				orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

	}

	@GetMapping("/all")
	private ResponseEntity<List<ContractDTO>> getAll() {

		List<ContractDTO> contractDTOS = contractMapper.contractsToContractsDTO(contractService.findAll());
		return new ResponseEntity<>(contractDTOS, HttpStatus.OK);

	}

	@PostMapping()
	private ResponseEntity<ContractDTO> createContract(@Valid @RequestBody ContractFormDTO contractFormDTO, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Contract contract = contractMapper.contractFormDTOtoContract(contractFormDTO);

		Optional<UserAccount> client = userAccountService.findById(contractFormDTO.getClientId());

		if (client.isPresent()) {
			contract.setClient(client.get());
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Optional<Victim> victim = victimService.findById(contractFormDTO.getVictimId());

		if (victim.isPresent()) {
			contract.setVictim(victim.get());
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Optional<District> district = districtService.findById(contractFormDTO.getDistrictId());

		if (district.isPresent()) {
			contract.setDistrict(district.get());
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		contract = contractService.save(contract);
		ContractDTO contractDTO = contractMapper.contractToContractDTO(contract);

		return new ResponseEntity<>(contractDTO, HttpStatus.CREATED);
	}

	@PutMapping()
	private ResponseEntity<ContractDTO> updateContract(@Valid @RequestBody ContractDTO contractDTO, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(contractDTO, HttpStatus.BAD_REQUEST);
		}

		Optional<Contract> contract = contractService.findById(contractDTO.getId());

		if (contract.isPresent()) {
			contractService.save(contractMapper.contractDTOtoContract(contractDTO));
			return new ResponseEntity<>(contractDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(contractDTO, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<ContractDTO> deleteContract(@PathVariable Long id) {

		Optional<Contract> contract = contractService.findById(id);

		if (contract.isPresent()) {
			contractService.deleteById(id);
			return new ResponseEntity<>(null, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

		}
	}


}
