package com.mongodb.quickstart;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.quickstart.models.Grade;
import com.mongodb.quickstart.models.Score;

public class POJODateSearches {

	// To search in compass use filter: {student_id: {$eq: 10003.0}}
	public static void main(String[] args) {
		
        ConnectionString connectionString = new ConnectionString(System.getProperty("mongodb.uri"));
        
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                                                                .applyConnectionString(connectionString)
                                                                .codecRegistry(codecRegistry)
                                                                .build();
        
        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
        	
            MongoDatabase db = mongoClient.getDatabase("sample_training");
            MongoCollection<Grade> grades = db.getCollection("grades", Grade.class);
            
            String dateTimeString = "2023-12-30T12:30:45";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

            // create a new grade.
            Grade newGrade = new Grade().setStudentId(10003d)
                                        .setClassId(10d)
                                        .setScoreDate(localDateTime)
                                        .setScores(List.of(new Score().setType("homework").setScore(50d)));
            grades.insertOne(newGrade);
            System.out.println("Grade inserted.");

            // find this grade.
            Grade grade = grades.find(eq("student_id", 10003d)).first();
            System.out.println("Grade found:\t" + grade);

            // update this grade: adding an exam grade
            List<Score> newScores = new ArrayList<>(grade.getScores());
            newScores.add(new Score().setType("exam").setScore(42d));
            grade.setScores(newScores);
            Bson filterByGradeId = eq("_id", grade.getId());
            FindOneAndReplaceOptions returnDocAfterReplace 
            	= new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
            
            Grade updatedGrade = grades.findOneAndReplace(filterByGradeId, grade, returnDocAfterReplace);
            System.out.println("Grade replaced:\t" + updatedGrade);

            // exact date search
            Grade grade_result1 = grades.find(eq("scoreDate", localDateTime)).first();        
            
            System.out.println("Found grade:\t" + grade_result1);
                        
            // Option 1: date range search
            LocalDateTime from1 = LocalDateTime.of(2023, 12, 29, 12, 0, 0);
            LocalDateTime to1 = LocalDateTime.of(2023, 12, 31, 12, 0, 0);
            
            BasicDBObject object1 = new BasicDBObject();
            object1.put("scoreDate", BasicDBObjectBuilder.start("$gte", from1).add("$lte", to1).get());
            List<Grade> list1 = new ArrayList<>(grades.find(object1).into(new ArrayList<>()));

            System.out.println("Found grade (list1):\t" + list1);
            
            // Option 2: date range search with timestamp strings
            String dateTimeFromStr = "2023-12-29T12:00:00";
            LocalDateTime from2 = LocalDateTime.parse(dateTimeFromStr, formatter);

            String dateTimeToStr = "2023-12-31T12:00:00";
            LocalDateTime to2 = LocalDateTime.parse(dateTimeToStr, formatter);
            
            BasicDBObject object2 = new BasicDBObject();
            object2.put("scoreDate", BasicDBObjectBuilder.start("$gte", from2).add("$lte", to2).get());
            List<Grade> list2 = new ArrayList<>(grades.find(object2).into(new ArrayList<>()));

            System.out.println("Found grade (list2):\t" + list2);	
            
            // delete this grade
//            System.out.println("Grade deleted:\t" + grades.deleteOne(filterByGradeId));

     }
	}
}
