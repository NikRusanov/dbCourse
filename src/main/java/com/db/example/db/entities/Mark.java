package com.db.example.db.entities;

import javax.persistence.*;

@Entity
@Table(name = "marks", schema = "main")
public class Mark {

    @Id
    @Column(name= "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="value")
    private int value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private  People student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private  People teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "subject_id")
    private Subject subject;


    public Mark() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public People getStudent() {
        return student;
    }

    public void setStudent(People student) {
        this.student = student;
    }

    public People getTeacher() {
        return teacher;
    }

    public void setTeacher(People teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
