package ru.job4j.cars.model;

import lombok.Data;
import javax.persistence.*;


@Entity
@Table(name = "photos")
@Data
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "url", nullable = true)
    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;
}

