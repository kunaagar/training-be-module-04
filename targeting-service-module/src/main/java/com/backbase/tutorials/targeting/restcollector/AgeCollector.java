package com.backbase.tutorials.targeting.restcollector;

import com.backbase.portal.targeting.connectorframework.content.contexts.definition.ResultEntries;
import com.backbase.portal.targeting.connectorframework.content.contexts.definition.SegmentDefinition;
import com.backbase.portal.targeting.connectorframework.content.contexts.definition.SelectorDefinition;
import com.backbase.portal.targeting.connectorframework.content.contexts.definition.StaticContextCollector;
import com.backbase.portal.targeting.rulesengine.type.RuleEngineTypes;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AgeCollector extends StaticContextCollector {

    private static final String AGE = "Age";
    private static final String BIRTHDAY = "birthDay";

    private static final String[] SEGMENTS = new String[]{
            "Youth",           //       age < 18
            "Getting Started", // 18 <= age < 35
            "Builders",        // 35 <= age < 50
            "Accumulators",    // 50 <= age < 60
            "Preservers"       // 60 <= age
    };
    private static final int[] AGE_LIMITS = new int[]{
            18,
            35,
            50,
            60
    };

    public AgeCollector() {
        super("ageCollector",
                "Collects information about the logged-in customer's age",
                "$(contextRoot)/static/targeting/icon/age_collector.png",
                "Age");
    }

    @Override
    public List<SelectorDefinition> defineSelectors(String portal, Map<String, String> properties) {
        List<SelectorDefinition> selectorDefinitions = new ArrayList<SelectorDefinition>();

        SelectorDefinition ageDefinition = new SelectorDefinition(AGE, RuleEngineTypes.INTEGER, "Age");
        ageDefinition.setDescription("Age of the customer");
        selectorDefinitions.add(ageDefinition);

        return selectorDefinitions;
    }

    @Override
    public List<SegmentDefinition> defineSegments(String portal, Map<String, String> properties) {
        List<SegmentDefinition> segmentDefinitions = new ArrayList<SegmentDefinition>();
        for (int i = 0; i < SEGMENTS.length; i++) {
            segmentDefinitions.add(new SegmentDefinition(SEGMENTS[i], "Age Segment: " + SEGMENTS[i]));
        }
        return segmentDefinitions;
    }

    @Override
    public ResultEntries executeTask(Map<String, String> requestMap, ResultEntries resultEntries) {

        //StaticContextCollector stores user profiles locally for future retrieval
        if (resultEntries.isEmpty()) {

            //get the username of the currently logged-in user
            String userName = requestMap.get("session.authentication.username");

            //configure the REST client
            Client client = ClientBuilder.newClient();

            //TODO externalize the URL
            WebTarget target = client.target("http://localhost:9999/training-server/rest/player/").path(userName);

            try {
                int age = 0;
                String jsonDataforPlayer = target.request(MediaType.APPLICATION_JSON).get(String.class);

                JSONObject resultAsJson = new JSONObject();

                //Simple JSON parsing using JSONSerializer
                resultAsJson = (JSONObject) JSONSerializer.toJSON(jsonDataforPlayer);
                if (resultAsJson.containsKey(BIRTHDAY)) {

                    long storedDateofBirth = (Long) resultAsJson.get("birthDay");
                    age = this.calculateAge(new Date(storedDateofBirth));

                }


                resultEntries.addSelectorEntry(AGE, Integer.toString(age));

                //set a segment
                resultEntries.addSegmentEntry(getSegmentId(age));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        return resultEntries;
    }

    /**
     * Simple Age calculation method with the help of JodaTime
     * @param dob date of birth of the person
     * @return int Age of the person
     */
    private int calculateAge(Date dob) {
        LocalDate dateofbirth = new LocalDate(dob);
        LocalDate today = new LocalDate();
        Period period = new Period(dateofbirth, today, PeriodType.yearMonthDay());
        int age = period.getYears();
        return age;
    }

    private static String getSegmentId(int age) {
        for (int i = 0; i < SEGMENTS.length - 1; i++) {
            if (age < AGE_LIMITS[i]) {
                return SEGMENTS[i];
            }
        }
        return SEGMENTS[SEGMENTS.length - 1];
    }

}
