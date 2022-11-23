package com.sb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.exception.ResourceNotFoundException;
import com.sb.model.Item1;
import com.sb.repository.ItemRepository;
import com.sb.service.ItemService;


@RequestMapping("/proApi")
@RestController

public class ItemController {

	@Autowired

	ItemService itemService;

	@Autowired
	ItemRepository itemRepository;

	@PostMapping("/create")
	public ResponseEntity<Item1> createItem(@Valid @RequestBody Item1 item) {
		try {
			Item1 t = itemService.saveOrUpdate(item);
			return new ResponseEntity<>(t, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/item/{id}")
	public ResponseEntity<Item1> getItemById(@PathVariable(value = "id") Long itemId) throws ResourceNotFoundException {
		Item1 item = itemRepository.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Entered ProductId is not available in Database,Could you please check or try with other Emp Number :: "
								+ itemId));
		return ResponseEntity.ok().body(item);
	}

	@GetMapping("/readAll")
	public ResponseEntity<List<Item1>> getAllProducts(@RequestParam(required = false) String name) {
		try {
			List<Item1> items = new ArrayList<Item1>();

			if (name == null)
				itemRepository.findAll().forEach(items::add);
			else
				itemRepository.findByNameContaining(name).forEach(items::add);

			if (items.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(items, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PatchMapping("/ite/{id}/{cost}")
	public ResponseEntity<Item1> updateItemPartially(@PathVariable Long id, @PathVariable Integer cost) {
		try {
			Item1 item = itemRepository.findById(id).get();
			item.setCost(cost);
			return new ResponseEntity<Item1>(itemRepository.save(item), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
