package com.backbase.training.targeting.restcollector;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AgeCollector extends StaticContextCollector {

    private static final Logger logger = LoggerFactory.getLogger(AgeCollector.class);

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

    @Value("${training.server.host}")
    private String trainingServerHost;

    @Value("${training.server.http.port}")
    private String trainingServerPort;

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
        for (String SEGMENT : SEGMENTS) {
            segmentDefinitions.add(new SegmentDefinition(SEGMENT, "Age Segment: " + SEGMENT));
        }
        return segmentDefinitions;
    }

    @Override
    public ResultEntries executeTask(Map<String, String> requestMap, ResultEntries resultEntries) {

        //StaticContextCollector stores user profiles locally for future retrieval
        if(resultEntries.isEmpty()) {

            //get the username of the currently logged-in user
            String userName = requestMap.get("session.authentication.username");

            //configure the REST client
            Client client = ClientBuilder.newClient();

            WebTarget target = client.target(MessageFormat.format("http://{0}:{1}/training-server/rest/player/", trainingServerHost, trainingServerPort)).path(userName);
            logger.debug("Calling Training Server: " + target.getUri());
            try {
                int age = 0;
                String jsonDataForPlayer = target.request(MediaType.APPLICATION_JSON).get(String.class);

                //Simple JSON parsing using JSONSerializer
                JSONObject resultAsJson = (JSONObject) JSONSerializer.toJSON(jsonDataForPlayer);
                if(resultAsJson.containsKey(BIRTHDAY)) {
                    long storedDateOfBirth = (Long) resultAsJson.get("birthDay");
                    age = calculateAge(new Date(storedDateOfBirth));

                }

                resultEntries.addSelectorEntry(AGE, Integer.toString(age));

                //set a segment
                String segmentId = getSegmentId(age);
                resultEntries.addSegmentEntry(segmentId);
                logger.debug(MessageFormat.format("Targeting calculation result: [username={0}, age={1}, segment={2}]",
                                userName, age, segmentId)
                );
            } catch (Exception e) {
                logger.error("Error communicating with Training Server: ", e);
            }

        }

        return resultEntries;
    }

    private static int calculateAge(Date dob) {
        LocalDate dateOfBirth = new LocalDate(dob);
        LocalDate today = new LocalDate();
        Period period = new Period(dateOfBirth, today, PeriodType.yearMonthDay());
        return period.getYears();
    }

    private static String getSegmentId(int age) {
        for (int i = 0; i < SEGMENTS.length - 1; i++) {
            if(age < AGE_LIMITS[i]) {
                return SEGMENTS[i];
            }
        }
        return SEGMENTS[SEGMENTS.length - 1];
    }

}
