package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    public static final String COUNTRY_CODE_KEY = "alpha3";
    private final ArrayList<JSONObject> list = new ArrayList<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject country = jsonArray.getJSONObject(i);
                list.add(country);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        ArrayList<String> languageCodes = new ArrayList<>();
        for (JSONObject obj : list) {
            if (country.equals(obj.getString(COUNTRY_CODE_KEY))) {
                languageCodes.addAll(obj.keySet());
            }
        }
        languageCodes.remove("id");
        languageCodes.remove("alpha2");
        languageCodes.remove(COUNTRY_CODE_KEY);
        return languageCodes;
    }

    @Override
    public List<String> getCountries() {
        ArrayList<String> countryCodes = new ArrayList<>();
        for (JSONObject obj : list) {
            countryCodes.add(obj.getString(COUNTRY_CODE_KEY));
        }
        return countryCodes;
    }

    @Override
    public String translate(String country, String language) {
        for (JSONObject obj : list) {
            if (country.equals(obj.getString(COUNTRY_CODE_KEY))) {
                return obj.getString(language);
            }
        }
        return null;
    }
}
