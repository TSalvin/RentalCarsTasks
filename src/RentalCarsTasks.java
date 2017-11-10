import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

public class RentalCarsTasks {

 public static void main(String[] Arguments) {

  File f = new File("vehicles.json");
  if (f.exists() && !f.isDirectory()) {
   System.out.println("vehicles.json found\n");
  } else {
   System.out.println("Error: vehicles.json not found");
  }
  try {
   // Store the vehicles in a list
   List < Vehicle > vehicles = new ArrayList < Vehicle > ();
   JsonReader reader = new JsonReader(new FileReader(f));
   reader.beginObject();

   while (reader.hasNext()) {
    // Locate "Search"
    String name = reader.nextName();
    if (name.equals("Search")) {

     // Locate the "VehiclesList" array
     reader.beginObject();
     name = reader.nextName();
     if (name.equals("VehicleList")) {
      reader.beginArray();
      while (reader.hasNext()) {
       vehicles.add(getVehicle(reader));
      }
      reader.endArray();
     }
    } else {
     reader.skipValue();
    }
   }
   reader.endObject();
   reader.endObject();
   reader.close();

   System.out.println("-----TASK 1-----\n");
   printAscending(vehicles);
   System.out.println("\n-----TASK 2-----\n");
   printSpec(vehicles);
   System.out.println("\n-----TASK 3-----\n");
   printDescendingSuppliers(vehicles);
   System.out.println("\n-----TASK 4-----\n");
   printDescendingScores(vehicles);

  } catch (IOException e) {
   e.printStackTrace();
  }
 }

 private static void printDescendingScores(List < Vehicle > vehicles) {
  // Print a list of all the cars, in descending score order .
  vehicles.sort(Comparator.comparing(Vehicle::getTotalScore, (s1, s2) -> {
   return s2.compareTo(s1);
  }));
  for (Vehicle v: vehicles) {
   System.out.println(
    "{" + v.getName() + "} - {" + v.getVehicleScore() + "} - {" + v.getRating() + "} - {" + v.getTotalScore() + "}"
   );
  }
 }
 private static void printDescendingSuppliers(List < Vehicle > vehicles) {

  // Print a list of all the cars, in descending supplier rating per car type
  vehicles.sort(Comparator.comparing(Vehicle::getRating, (s1, s2) -> {
   return s2.compareTo(s1);
  }));
  for (Vehicle v: vehicles) {
   char currentSipp[] = v.getSipp().toCharArray();
   System.out.println(
    "{" + v.getName() + "} - {" + getSpec("carType", currentSipp[0]) + "} - {" + v.getSupplier() + "} - {" + v.getRating() + "}"
   );
  }
 }

 private static void printAscending(List < Vehicle > vehicles) {
  // Print a list of all the cars, in ascending price order according to format
  vehicles.sort(Comparator.comparing(Vehicle::getPrice));
  for (Vehicle v: vehicles) {
   System.out.println("{" + v.getName() + "} - {" + v.getPrice() + "}");
  }
 }
 private static String getSpec(String category, char letterCode) {
  // Create hashmaps for the different categories and their codes
  HashMap < Character, String > carType = new HashMap < Character, String > ();
  carType.put('M', "Mini");
  carType.put('E', "Economy");
  carType.put('C', "Compact");
  carType.put('I', "Intermediate");
  carType.put('S', "Standard");
  carType.put('F', "Full size");
  carType.put('P', "Premium");
  carType.put('L', "Luxury");
  carType.put('X', "Special");

  HashMap < Character, String > doorsCarType = new HashMap < Character, String > ();
  doorsCarType.put('B', "2 doors");
  doorsCarType.put('C', "4 doors");
  doorsCarType.put('D', "5 doors");
  doorsCarType.put('W', "Estate");
  doorsCarType.put('T', "Convertible");
  doorsCarType.put('F', "SUV");
  doorsCarType.put('P', "Pick up");
  doorsCarType.put('V', "Passenger Van");

  HashMap < Character, String > transmission = new HashMap < Character, String > ();
  transmission.put('M', "Manual");
  transmission.put('A', "Automatic");

  HashMap < Character, String > fuelAirCon = new HashMap < Character, String > ();
  fuelAirCon.put('N', "Petrol/no AC");
  fuelAirCon.put('R', "Petrol/AC");

  switch (category) {
   case "carType":
    return carType.get(letterCode);
   case "doorsCarType":
    return doorsCarType.get(letterCode);
   case "transmission":
    return transmission.get(letterCode);
   case "fuelAirCon":
    return fuelAirCon.get(letterCode);
   default:
    System.out.println("Invalid category provided");
    return "null";
  }
 }
 private static void printSpec(List < Vehicle > vehicles) {
   // Print the specification of the vehicles based on their SIPP
   for (Vehicle v: vehicles) {
    char currentSipp[] = v.getSipp().toCharArray();
    System.out.println(
     "{" + v.getName() + "} - {" + v.getSipp() + "} - {" + getSpec("carType", currentSipp[0]) + "} - {" + getSpec("doorsCarType", currentSipp[1]) + "} - {" + getSpec("transmission", currentSipp[2]) + "} - {" + getSpec("fuelAirCon", currentSipp[3]) + "}");
   }
  }
  // Assumes each vehicle is wrapped as an object inside the vehicleList array
 private static Vehicle getVehicle(JsonReader reader) {
   String sipp = "null";
   String name = "null";
   String supplier = "null";
   double price = -1.0;
   double rating = -1.0;
   try {
    reader.beginObject();
    while (reader.hasNext()) {
     String nextName = reader.nextName();
     switch (nextName) {
      case "sipp":
       sipp = reader.nextString();
       break;
      case "name":
       name = reader.nextString();
       break;
      case "price":
       price = Double.valueOf(reader.nextString());
       break;
      case "supplier":
       supplier = reader.nextString();
       break;
      case "rating":
       rating = Double.valueOf(reader.nextString());
       break;
      default:
       reader.skipValue();
       break;
     }
    }
    reader.endObject();
   } catch (IOException e) {
    e.printStackTrace();
   }
   return new Vehicle(sipp, name, supplier, price, rating);
  }
  // each vehicle in the JSON document is imported as a Vehicle object
 private static class Vehicle {
  public String sipp;
  public String name;
  public String supplier;
  public double price;
  public double rating;

  private Vehicle(String sipp, String name, String supplier, double price, double rating) {
   this.sipp = sipp;
   this.name = name;
   this.supplier = supplier;
   this.price = price;
   this.rating = rating;
  }

  public String getSipp() {
   return this.sipp;
  }
  public String getName() {
   return this.name;
  }
  public String getSupplier() {
   return this.supplier;
  }
  public Double getPrice() {
   return this.price;
  }
  public Double getRating() {
   return this.rating;
  }

  public double getVehicleScore() {
   int score = 0;
   // Add breakdown score
   char currentSipp[] = this.getSipp().toCharArray();
   if (getSpec("transmission", currentSipp[2]) == "Manual") {
    score += 1;
   } else if (getSpec("transmission", currentSipp[2]) == "Automatic") {
    score += 5;
   };
   if (getSpec("fuelAirCon", currentSipp[3]) == "Petrol/AC") {
    score += 2;
   };
   return score;
  }
  public double getTotalScore() {
   return this.getVehicleScore() + this.getRating();
  }
 }

}