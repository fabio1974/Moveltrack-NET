package com.moveltrack.cartaoprograma.rest.get.specification;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationsBuilder<T> {

	public Specification<T> buildEspecification(String search) {
		SpecificationsBuilderEngine<T> builder = new SpecificationsBuilderEngine<T>();
		Pattern pattern = Pattern.compile("(\\w+?)(:|<=|<|>|>=)(\\w+?),");
		Matcher matcher = pattern.matcher(search + ",");
		while (matcher.find()) {
			builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
		}
		Specification<T> spec = builder.build();
		return spec;
	}
	
	
	
	
	

}
