package com.bubbles.learning;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bubbles.learning.converter.DatToJsonConverter;

@RestController
public class ApiController {

	@PostMapping("/converter")
	public ResponseEntity<String> convert(@RequestBody String requestBody) {
		List<String> toConvert = requestBody.lines().toList();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(DatToJsonConverter.convert(toConvert), responseHeaders, HttpStatus.OK);
	}

}
