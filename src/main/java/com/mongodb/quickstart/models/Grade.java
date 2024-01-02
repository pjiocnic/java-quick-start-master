package com.mongodb.quickstart.models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Grade {

    private ObjectId id;
    @BsonProperty(value = "student_id")
    private Double studentId;
    @BsonProperty(value = "class_id")
    private Double classId;
    private List<Score> scores;
    private LocalDateTime scoreDate;

    public ObjectId getId() {
        return id;
    }

    public Grade setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public Double getStudentId() {
        return studentId;
    }

    public Grade setStudentId(Double studentId) {
        this.studentId = studentId;
        return this;
    }

    public Double getClassId() {
        return classId;
    }

    public Grade setClassId(Double classId) {
        this.classId = classId;
        return this;
    }

    public List<Score> getScores() {
        return scores;
    }

    public Grade setScores(List<Score> scores) {
        this.scores = scores;
        return this;
    }
    
    public LocalDateTime getScoreDate() {
    	return scoreDate;
    }

    public Grade setScoreDate(LocalDateTime dt) {
        this.scoreDate = dt;
        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Grade{");
        sb.append("id=").append(id);
        sb.append(", student_id=").append(studentId);
        sb.append(", class_id=").append(classId);
        sb.append(", scores=").append(scores);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Grade grade = (Grade) o;
        return Objects.equals(id, grade.id) && Objects.equals(studentId, grade.studentId) && Objects.equals(classId,
                                                                                                            grade.classId) && Objects
                .equals(scores, grade.scores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, classId, scores);
    }
}
