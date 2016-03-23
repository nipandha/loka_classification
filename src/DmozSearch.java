import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class DmozSearch {

	public int phrase_search(String x,String y)
	{
		x=x.trim();
		y=y.trim();
		int cx=0,cy=0,skipped_words=0;
		String[] lx = x.split("\\W+");
		String[] ly = y.split("\\W+");
		for(;cx<lx.length;cx++)
		{
			int tempy=cy;
			int foundcx=0;
			while(tempy<ly.length)
			{
				if(StringUtils.getLevenshteinDistance(lx[cx],ly[tempy])==0)
				{
					
					cy=tempy;
					foundcx=1;
					break;
				}
				else if((StringUtils.getLevenshteinDistance(lx[cx],ly[tempy])<3)) //Assume as spelling mistake
				{
					//Accept only one spelling mistake
					if(simple_phrase_check(x.replace(lx[cx], ""),y.replace(ly[tempy], ""))==true)
						return 1;
					else
					{
						
						tempy++;
					}
				}
				else
				{
					
					tempy++;
				}
			}
			if(foundcx==0)
			{
				skipped_words++;
				//System.out.println("Skipped word "+lx[cx]);
			}
		}
		if(skipped_words==0)
		{
			return 0;
		}
		if(skipped_words<=(lx.length/2))
		{
			return 1;
		}
		System.out.println(x+" "+y+" "+skipped_words);
		return -1;
	}
	public boolean simple_phrase_check(String x,String y)
	{
		x=x.trim();
		y=y.trim();
		String[] lx = x.split("\\W+");
		String[] ly = y.split("\\W+");
		if(lx.length!=ly.length)
			return false;
		for(int i=0;i<lx.length;i++)
		{
			if(lx[i]!=ly[i])
				return false;
		}
		return true;
	}
	public void searchFile(List<String> l,String resPath)
	{
		try {
			String filename=new File(resPath).getAbsolutePath();
			Path path = FileSystems.getDefault().getPath(filename);
			Files.deleteIfExists(path);
			final FileWriter sw = new FileWriter(filename);
			final CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
			List Record = new ArrayList();
			Record.add("D");
			Record.add("S");
			Record.add("Exact Match");
			//DataRecord.add(mapping);
		    printer.printRecord(Record);
			for(String seed: l)
			{
				//BufferedReader br = new BufferedReader(new FileReader("resources/categories.txt"));
				BufferedReader br = new BufferedReader(new FileReader("resources/democategories.txt"));
				for(String line; (line = br.readLine()) != null; ) {
					if(phrase_search(line,seed)==0)
					{
						List DataRecord = new ArrayList();
						DataRecord.add(line);
						DataRecord.add(seed);
						DataRecord.add("Yes");
						//DataRecord.add(mapping);
					    printer.printRecord(DataRecord);
					}
					else if(phrase_search(line,seed)==1)
					{
						List DataRecord = new ArrayList();
						DataRecord.add(line);
						DataRecord.add(seed);
						DataRecord.add("No");
						//DataRecord.add(mapping);
					    printer.printRecord(DataRecord);
					}
				}
			}
			printer.flush();
			printer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String args[])
	{
		String[] stemmedseed = {"Honeybee Hive" , "Lotus" , "Nelumbo" , "Biodiversity" , "seed"  , "Farm Tourism" , "Gardening" , "Organic Farming" , "Agniastra" , "Amrut Jal" , "Azolla" , "Bijamrita" , "Bio Char" , "Brahmastra" , "Compost Tea" , "Dasparni" , "Dhrava jeevamrita" , "Ghana jeevamruta" , "Green Manure" , "Green Planet Bio Fertilizer" , "Growing Giant Vegetables" , "Hareet Sanjivani" , "Herbal Insecticide" , "Honey Hormone" , "Improving Saline Land" , "Insecticide from Buttermilk" , "Jeevamruta" , "Magic Tonic & Compost" , "Matka Khad" , "Nimastra" , "Panchagavya" , "Pullatimajjiga Dhravanam" , "Seaweeds ","Fertilizer" , "Cow Breed", "Grass ","Cereal" , "Legume" , "Agnihotra" , "Alley Cropping", "Silvopasture" , "Bhaskar Save" , "Biodynamics", "Biointensive" , "Companion Planting" , "Conservation Agriculture" , "Growing Vegetables" , "shade plant" , "Hydroponics" , "Mix Crop" , "Natural Farming" , "Permaculture" , "Natueco Farming" , "Music Therapy" , "Raised Bed Farming" , "Ratooning" , "Sloping Agriculture" , "Sonic Bloom" , "Square feet gardening" , "Subhash Sharma" , "Crop Intensification" , "Urban Gardening" , "Meadow" , "Wildflowers" , "Water Conservation" , "soil conservation", "timber" , "Murals" , "Painting" , "Dyeing  Techniques" , "Weaving" , "Natural Fibres" , "Orinetal Rugs" , "Textiles" , "Vegetable Dyes" , "Irrigation", "Water Harvesting", "Groundwater" , "Irrigation", "Water" , "Water Lifting Devices" ,  "Mud Housing" , "Natural Paints" , "Consruction Material" , "Traditional Housing Practises" , "Archery" , "General" , "Lance" , "Shield" , "Sword" , "Damascus Steel" , "Iron","Steel" , "Mining"," Production of Various Metals  Pottery" , "Ports","Harbours" , "Marine Life" , "Churning Cultured Butter" ,"Butter", "Continuous butter making  Butter" , "Dazey Glass Jar Butter Churns" , "Directions for Making Butter" , "dehusk","rice","De-husking rice with a wooden beater in Himalayas - 1990" , "Water-powered rice-pounding mortar" , "Water Powered"," Flour Mill" , "Water Wheel Powered Grain Mill" ,"grain-Mill", "hotbeds of invention" , "IMPROVED CHULHAS" , "Labour of Love" , "Mill Creek Sawmill" , "Pedal Power" , "Rajkumar Hand Operated Oil Expeller Gives you pure oil within minutes" ,"oil expeller", "Waa Siu- In the Making of Beaten-Rice" , "Water powered rice cleaning mill" , "Handmade Paper and Boards" , "Handmade Paper","Handmade Papermaking" , "How to on Handmade Paper" , "Jaggery Making" ,"jaggery", "Making Sugar from Coconut Palm" , "Making Sugar from Date Palm" , "Making Sugar From Toddy Palm" , "Kurmura - Puffed Rice Making" , "Kurmura Chikki" , "Poha - Beaten Rice" , "Sorghum Popping" , "A Documentary on Goan Handicrafts" , "A Selection of Gond Tribal Art" , "Carpet - Rug & its Weaving" , "Jharcraft" , "Leaf Plate Making" , "Madhubani Painting" , "Making of Charpoy" , "Mandana Printing" , "Mirror Work" , "Mud Missive" , "Pichwai Art" , "Rope Making" , "Stone Art" , "Thatch Roof Technique" , "gaint vegetables","craftsmen" , "Warli Art" , " tiller","harrow"};
		String[] seed = {"Honey from our Stingless Bee Hive" , "Growing a Hardy Lotus" , "Growing Lotus Plant" , "Growing Nelumbo From Seed & Seedling Cultivationl" , "Lotus Planting & Growing Instructions" , "Biodiversity in Agriculture" , "Ancient technologies of seed fertilization" , "Biodiversity and its Conservation" , "Farm Tourism" , "Gardening" , "Organic Farming - Farmers" , "Agniastra" , "Amrut Jal" , "Azolla" , "Bijamrita" , "Bio Char" , "Brahmastra" , "Compost Tea" , "Dasparni" , "Dhrava jeevamrita" , "Ghana jeevamruta" , "Green Manure" , "Green Planet Bio Fertilizer" , "Growing Giant Vegetables" , "Hareet Sanjivani" , "Herbal Insecticide" , "Honey Hormone" , "Improving Saline Land" , "Insecticide from Buttermilk" , "Jeevamruta" , "Magic Tonic & Compost" , "Matka Khad" , "Nimastra" , "Panchagavya" , "Pullatimajjiga Dhravanam" , "Seaweeds as Fertilizer" , "Cow Breeds of Various Countries" , "Grass (Cereal)" , "Grass & Legume Seeds" , "A new way of organic farming.jpg" , "Agnihotra" , "Alley Cropping - Silvopasture" , "Bhaskar Save" , "Bio Dynamics Farming" , "Bio Intensive Farming" , "Companion Planting" , "Conservation Agriculture" , "Growing Veges from Cuttings-suckers" , "Growing Vegetables Under Shade" , "Hydroponics" , "Mixed Cropping" , "Natural Farming" , "Permaculture" , "Prayog Parivar - Natueco Farming" , "Raga - Music Therapy" , "Raised Bed Farming" , "Ratooning" , "Sloping Agri Land Tech & Allied" , "Sonic Bloom" , "SQ FT Gardening" , "Subhash Sharma" , "System of Crop Intensification" , "Urban Gardening & Solution" , "Meadow - Wild Flowers" , "How to Grow Wildflowers _ P. Allen Smith Classics" , "Resources for Making Meadows" , "Innovations in farming practices" , "Water Conservation" , "soil and water conservation methods" , "Early Medieval timber work" , "Murals" , "Painting" , "Different Manu & Dyeing Techniques" , "Different Steps in Weaving" , "Natural Fibres" , "Orinetal Rugs" , "Textiles" , "Vegetable Dyes" , "Ancient - Modern Irrigation & Water Harvesting Around the World" , "Groundwater" , "Irrigation for Agriculture" , "Water" , "Water Lifting Devices" , "Construction of Ancient Civilization" , "Mud Housing" , "Natural Paints" , "Traditional Consruction Material" , "Traditional Housing Practises" , "Archery" , "General" , "Lance" , "Shield" , "Sword" , "Damascus Steel" , "Iron & Steel Making" , "Mining & Production of Various Metals  Pottery" , "Ancient Ports & Harbours" , "Marine Life" , "Churning Cultured Butter" , "Continuous butter making  Butter" , "Dazey Glass Jar Butter Churns" , "Directions for Making Butter" , "De-husking rice with a wooden beater in Himalayas - 1990" , "Water-powered rice-pounding mortar" , "Water Powered Flour Mill" , "for item in seed_set :Water Wheel Powered Grain Mill" , "hotbeds of invention" , "IMPROVED CHULHAS" , "Labour of Love" , "Mill Creek Sawmill" , "Pedal Power" , "Rajkumar Hand Operated Oil Expeller Gives you pure oil within minutes" , "Waa Siu- In the Making of Beaten-Rice" , "Water powered rice cleaning mill" , "Handmade Paper and Boards" , "Handmade Paper Making Operation in Kathmandu_ Nepal" , "Handmade Papermaking" , "How to on Handmade Paper" , "Jaggery Making" , "Making Sugar from Coconut Palm" , "Making Sugar from Date Palm" , "Making Sugar From Toddy Palm" , "Kurmura - Puffed Rice Making" , "Kurmura Chikki" , "Poha - Beaten Rice" , "Sorghum Popping" , "A Documentary on Goan Handicrafts" , "A Selection of Gond Tribal Art" , "Carpet - Rug & its Weaving" , "Jharcraft" , "Leaf Plate Making" , "Madhubani Painting" , "Making of Charpoy" , "Mandana Printing" , "Mirror Work" , "Mud Missive" , "Pichwai Art" , "Rope Making" , "Stone Art" , "Thatch Roof Technique" , "The craftsmen of Gujarat   (India)" , "Warli Art" , " tiller and harrow"};
		String[] seed2={"Mixed Cropping" , "mixed crop" , "Plough" , "Thresher" , "Ancient Agri Practices" , "Agriculture" , "Crop Protection" , "Seed - Grain & Vegetable Storage Systems" , "Bee Keeping" , "bee" , "Biodiversity" , "Compost" , "Herbal" , "Insecticide" , "Cows" , "Different Farming Methods" , "Gardens" , "Gardening" , "Modern Agri Problems" , "Infrastructure" , "Schemes" , "organic" , "inorganic" , "Basketry" , "Carpentry" , "Art" , "Handloom" , "Irish Round Towers" , "Irrigation" , "Lime" , "Mortar" , "Martial Arts" , "Metallurgy" , "Rain Prediction" , "Rain" , "Sacred Groves" , "Shipping" , "Transport" , "Starting Fire" , "Butter Churn" , "butter" , "dehusking" , "Dehusking of Rice" , "Flour Mill" , "General" , "Handmade Paper" , "Jaggery" , "Sugar" , "Cereals" , "Art" , "dyeing" , "Pump" , "Banana" , "Application of indigenous technology in water" , "Automatic Pump Operator" , "Bamboo processing machine" , "Slicer " , "Bicycle Sprayer" , "Bicycle weeder" , "biogas" , "biogeyser" , "Biogeyser - Harnessing heat of biodigestion" , "Water Pump" , "Sprayer" , "cassava" , "coconut" , "Coconut Dehusker" , "gourd" , "drip irrigation" , "thresher" , "multicrop " , "innovation" , "groundnut" , "Conservation of traditional variety of Tinda (round gourd)" , "Conversion of plain power looms into continuous weft feeding looms" , "Cycle operated water-lifting pump" , "Double Acting Reciprocating Pump" , "Double Flywheel Multicrop Threshers" , "Drip Irrigation  Drip Irrigation kit Drum Kit Bucket kit KB Drip" , "Dual purpose Rotary Huller  National Innovation Foundation" , "Earthen kitchen products" , "Farm implement - Gangtho" , "Forecasting Rain - Neem" , "Ground Nut Pod Separator" , "Gum Scrapper" , "Inventor of no-electricity refrigerator" , "Jailaxmi Power Thresher" , "Jitabhai Kodarbhai Patel" , "JUGAAD Innovation" , "Kambalwadi - Transformation of a Village" , "Kandap Machine" , "Laxmi Asu Making Machine" , "Living Traditions - How Do We Preserve Them" , "Low-cost milking machine" , "Lowcost labour-saving seed drill" , "Manual Tile Making Machine  Business Development" , "Washing Machine" , "Manual Wood Cutting Machine  Business Development" , "Milking Machine" , "Mitti Cool Refrigerator An affordable refrigerator made of clay  GIA" , "Modification in Jacquard loom for extra weft insertion" , "Multi Cylinder Reciprocating Pump  Business Development" , "MULTI-FUNCTIONAL FARM MACHINE  GIAN" , "Multipurpose Machine  National Innovation Foundation" , "Multipurpose twin chambered cooking vessel and others" , "Mysore Mallige- A unique paddy variety" , "Natural Water Cooler" , "Non Stick Coated Earthen (Clay) Tawa" , "Oil Expeller" , "Paddy planter" , "Palm and Coconut Leaf Mat Weaving Machine" , "Plants as Ecological Indicators of Weather" , "Poha Mill" , "Pooran Pump _ National Innovation Foundation" , "Pooran Pump" , "Potato Cultivation in Hay Sacks" , "Power Saving Technical Pump" , "Rain Gun - Saga of a Sprinkler Designer" , "Rain Prospecting on Ancient Beliefs" , "water conservation" , "Rural water lifting devices" , "Seed cum fertilizer drill machine" , "seedrill" , "SMALL BUNDLE THRESHER" , "solarmos" , "Food grain" , "Suraksha Tong" , "Tamrind Harvester & seed separator" , "The all-purpose grinding machine" , "The brick making machine - funny invention" , "Tractor Model Thresher" , "Treadle Pump 3_5 inch pump (metal barrels) with metal treadles" , "treeclimber" , "Village Fridge" , "pulverizer" , "Weather forecasting" , "Wheat sowing plate" };
		String[] seed3={"agriculture","tradition","innovation","technology","ancient"," Education" , "Training" , "Livelihoood" , "Health" , "Innovations" , "Natural Resources"};
		DmozSearch d = new DmozSearch();
		String[] trial={"wars food","love in air","innovation","nitisha pitisha","bleh bleh bleh"};
		d.searchFile(Arrays.asList(trial),"trial.csv");
		
	}
}
