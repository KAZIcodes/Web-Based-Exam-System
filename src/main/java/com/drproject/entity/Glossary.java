package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name="glossary")
public class Glossary extends Activity{
    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "UUID", columnDefinition = "VARCHAR(36)")
    private UUID id;

}
