package org.restaurant.api.controller;

import entity.KhachHang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.restaurant.repository.KhachHangRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/khach-hang")
@CrossOrigin(origins = "*")
public class KhachHangController {

	@Autowired
	private KhachHangRepository khachHangRepository;

	@GetMapping
	public ResponseEntity<List<KhachHang>> getAllKhachHang() {
		List<KhachHang> khachHangs = khachHangRepository.findAll();
		return ResponseEntity.ok(khachHangs);
	}

	@GetMapping("/{id}")
	public ResponseEntity<KhachHang> getKhachHangById(@PathVariable String id) {
		Optional<KhachHang> khachHang = khachHangRepository.findById(id);
		return khachHang.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<KhachHang> createKhachHang(@RequestBody KhachHang khachHang) {
		KhachHang saved = khachHangRepository.save(khachHang);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<KhachHang> updateKhachHang(@PathVariable String id, @RequestBody KhachHang khachHang) {
		if (!khachHangRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		khachHang.setMaKH(id);
		KhachHang updated = khachHangRepository.save(khachHang);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteKhachHang(@PathVariable String id) {
		if (!khachHangRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		khachHangRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
