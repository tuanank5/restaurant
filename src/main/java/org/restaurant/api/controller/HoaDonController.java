package org.restaurant.api.controller;

import entity.HoaDon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.restaurant.repository.HoaDonRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hoa-don")
@CrossOrigin(origins = "*")
public class HoaDonController {

	@Autowired
	private HoaDonRepository hoaDonRepository;

	@GetMapping
	public ResponseEntity<List<HoaDon>> getAllHoaDon() {
		List<HoaDon> hoaDons = hoaDonRepository.findAll();
		return ResponseEntity.ok(hoaDons);
	}

	@GetMapping("/{id}")
	public ResponseEntity<HoaDon> getHoaDonById(@PathVariable String id) {
		Optional<HoaDon> hoaDon = hoaDonRepository.findById(id);
		return hoaDon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<HoaDon> createHoaDon(@RequestBody HoaDon hoaDon) {
		HoaDon saved = hoaDonRepository.save(hoaDon);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<HoaDon> updateHoaDon(@PathVariable String id, @RequestBody HoaDon hoaDon) {
		if (!hoaDonRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		hoaDon.setMaHD(id);
		HoaDon updated = hoaDonRepository.save(hoaDon);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteHoaDon(@PathVariable String id) {
		if (!hoaDonRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		hoaDonRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
