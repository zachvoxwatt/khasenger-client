package audio;

import java.io.InputStream;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.stream.JsonReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class JSONDataParser 
{
	private boolean reports = false;
	private Map<String, List<String>> dirmap;
	
	public JSONDataParser()
	{
		this.dirmap = null;
		
		InputStream is = this.getClass().getResourceAsStream("/_data/audio.json");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		JsonReader jr = new JsonReader(br);
		
		try 
		{ 
			dirmap = readJSONFile(jr);
			is.close();
			br.close();
			jr.close();
		}
		catch (Exception e) { e.printStackTrace(); }
		
		if (reports)
		{
			for (Map.Entry<String, List<String>> map: dirmap.entrySet())
			{
				System.out.println(map.getKey());
				
				if (!Objects.isNull(map.getValue()))
				{
					Iterator<String> itor = map.getValue().iterator();
					while (itor.hasNext()) System.out.printf("%s ",itor.next());
				}
				
				System.out.println();
				System.out.println();
			}
		}
	}
	   
	private Map<String, List<String>> readJSONFile(JsonReader reader) throws IOException
	{
		Map<String, List<String>> returner = new HashMap<>();
		
		String key = null, type = null;
		List<String> values = null;
		
		reader.beginArray();
		while (reader.hasNext())
		{
			//begin the entire object file
			reader.beginObject();
			
			while (reader.hasNext())
			{
				//get the next Token name
				String name = reader.nextName();
				
				//check token name 'id' to get the general name
				if (name.equals("id")) key = reader.nextString();
				else if (name.equals("type")) type = reader.nextString();
				else if (name.equals("various")) //check if property of token is 'various'
				{
					//add all variants to array
					boolean isVarious = reader.nextBoolean();
					if (isVarious)
					{
						name = reader.nextName();
						if (name.equals("variants"))
							values = processVariantsArray(reader, type); 
					}
					
					//if it isnt various, adds a single item to the value of the key
					else values = processSingleItem(reader, type, key);
					
					//finally, put into the map
					returner.put(key, values);
				}
				else reader.skipValue();
			}
			reader.endObject();
		}
		
		return returner;
	}
	
	private List<String> processSingleItem(JsonReader reader, String type, String key) throws IOException
	{
		List<String> returner = new ArrayList<>();
		String tba = "/audio/" + type + "/" + key + ".wav";
		returner.add(type);
		returner.add(tba);
		
		return returner;
	}
	
	private List<String> processVariantsArray(JsonReader reader, String type) throws IOException
	{
		List<String> returner = new ArrayList<>();
			returner.add(type);
		
		String tba = "/audio/";
		
		reader.beginArray();
		while (reader.hasNext())
		{
			tba += type + "/" + reader.nextString() + ".wav";
			
			returner.add(tba);
			tba = "/audio/";
		}
		reader.endArray();
		
		return returner;
	}
	
	public Map<String, List<String>> getDirectoryMap() { return this.dirmap; }
	
	public static void main(String[] ar) { EventQueue.invokeLater(JSONDataParser::new); }
}
