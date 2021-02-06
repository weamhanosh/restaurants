package com.restaurants.restaurants.controller;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.restaurants.restaurants.domain.Restaurant;

@RestController
public class RestaurantController {
	
	@Autowired
	private RestaurantControllerRepo repo;
	
	@Autowired
	private GeocodeController geocodeController;
	
	public List<Restaurant> loadCSV() throws IOException
	{
		
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setType(Restaurant.class);
		String[] columns = new String[] {"Name", "Type", "Phone", "Location"}; // the fields to bind do in your JavaBean
		strat.setColumnMapping(columns);

		CsvToBean<Restaurant> csv = new CsvToBean();

		String csvFilename = "./restaurants.csv";
		CSVReader csvReader = new CSVReader(new FileReader(csvFilename));

		
		List<Restaurant> list = csv.parse(strat, csvReader);
		for (Restaurant restaurant : list) 
		{
			System.out.println(restaurant.toString());
			String[] arrSplit = restaurant.getLocation().split("/");
			
		//	GeocodeResult  location = geocodeController.getGeocode(arrSplit[0],arrSplit[1]);
		//	System.out.println("location :" +  location);
				
		}
		
		return list;
	}

	@RequestMapping("/restaurants")
	@CrossOrigin(origins = "*")
	public List<Restaurant> getRestaurant()
	{
		Iterable<Restaurant> it = repo.findAll();
		
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		it.forEach(e -> restaurants.add(e));

	    return restaurants;
	}
	
	@RequestMapping("/restaurants/{id}")
	public Optional<Restaurant> getRestauranDetails(@PathVariable long id)
	{
		return repo.findById(id);
	}

}
