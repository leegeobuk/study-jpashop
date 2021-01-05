package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("album")
@NoArgsConstructor
@Getter @Setter
public class Album extends Item {

    private String artist;
    private String etc;

    public Album(Long id) {
        super(id);
    }
}
