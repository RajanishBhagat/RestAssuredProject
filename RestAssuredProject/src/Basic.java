import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.response.ValidatableResponseOptions;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.payload;

public class Basic {

	public static void main(String[] args) {
		
    System.out.println("*****Started to use POST Method Api******");
 		   //validate if Add Place API/ POST API is working as expected
    /**
     * There are three Principles/Methods  of Rest Assured  
     * given - all input details 
     * when - Submit the API -resource,http method
     * Then - validate the response
     */
		 
    //Set RestAssured baseURI  and import package io.restassured.RestAssured
    RestAssured.baseURI = "https://rahulshettyacademy.com";
    
   /** given() method working 
    * now start with given() method so for that sake import static package of restassured,
    * So import static io.restassured.RestAssured.*;
    * What we have done with POST Api with Google Map using Postman manually, we will do automation of it 
    * used quaryParam to pass quary parameter in terms key - value pair used  given().queryParam("key", "qaclick123")
    * if  remember we also select body >> row >> Json  for send the request in JSON format. It content- type as json will pass into the header
    * so concatinate with existing given() method as .header("Content-Type","application/json")
    * Now we need to give the body  so will again concatinate the body("just copy the request body json started with { and end with } and paste") with given() method
    * if json is not converted into the string when you will paste the json in body() method, must check window >> preferance >> Java >>editor >>Typing>> must check "Escape text when pasting into a string literal
    *
    * when() method working 
    *  when() method is used to set POST method and Resource URI
    *  concatinate with same chaining started with given() method use .when().post("maps/api/place/add/json")
    *  
    *  then() method working
    *  then()method used to vaidate response, Here validating when adding place then getting statuc code 200 or not.  
    *  again concatinate with same chaining .then().assertThat().statusCode(200);
    *  If you want to logging with each method use log().all() after the given() or when() or then() method. you will get all logs in eclipse console window
    *  given().log().all().queryParam("key", "qaclick123")..when().log().all()post("maps/api/place/add/json")
           .then().log().all().assertThat().statusCode(200); 
    */
   String response =  given().queryParam("key", "qaclick123").header("Content-Type","application/json").body(payload.AddPlace()).when().post("maps/api/place/add/json")
           .then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
  
    System.out.println("Response starting \n" + response);
    
     //for parsing Json use JsonPath and import io.restassured.path.json.JsonPath;
    JsonPath js=new JsonPath(response);
	String placeId=js.getString("place_id"); //place_id comes with response body when we add any palce with POST.
	
	System.out.println("placeId is \n" + placeId);
	
	
	/**
	 * PUT Method use means Update Place(different place name) for same place id (place_id must be same that comes from above POST Api Response body
	 * as i wrote manually place id, place id may change so should not write manually 
	 * so instead of this  ==>   "\"place_id\": \"843c75d3f0561bb807ccfce63a67add4\",\r\n" + 
	 * write =>>  "\"place_id\": \""+placeid+"\",\r\n" +  
	 * means oly change 843c75d3f0561bb807ccfce63a67add4  to  "+placeId+"  
	 * Actually placeId is retrieve from above during POST Operation   ==> String placeId=js.getString("place_id");
	 */
	
	System.out.println("*****Started to use PUT Method Api******");
	 given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
	 .body("{\r\n" + 
	 		"\"place_id\": \""+placeId+"\",\r\n" + 
	 		"\"address\":\"71 winter walk, USA\",\r\n" + 
	 		"\"key\":\"qaclick123\"\r\n" + 
	 		"}")
	 .when().log().all().put("maps/api/place/update/json")
	 .then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
	 
	 
	 /**
	  * Next for validation that PUT method is updated a new address or not,We will use GET Method/API to retrieve the request from server.
      * As we  know in GET api everything will be pass from URL only not the request body.
      * That’s why here with given() method no need to pass the header  like .header("Content-Type","application/json")
      * Because we are not passing the body. So no point for having the header.
      * If we remember in postman we also not passed any header that time.   
      * But we need to pass one more query parameter that is place_id .
      * Because with GET we need to tell what place id need to retrieve.
	  * Variable placeId that is used as  =>> .queryParam("place_id",placeId) is  derived from ==>> String placeId=js.getString("place_id");
	  */
	 System.out.println("*****Started to use GET Method Api******");
	 
	 String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id",placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().statusCode(200).extract().response().asString();
	 JsonPath js1=new JsonPath(getPlaceResponse);
		String actualAddress =js1.getString("address");
		System.out.println(actualAddress);
		//For use of validation though Assert, I add TestNG library with this project  
		Assert.assertEquals(actualAddress, "71 winter walk, USA");
      
}
}

