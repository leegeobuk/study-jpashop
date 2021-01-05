package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("movie")
@NoArgsConstructor
@Getter @Setter
public class Movie extends Item {

    private String director;
    private String actor;

    public Movie(Long id) {
        super(id);
    }
}
