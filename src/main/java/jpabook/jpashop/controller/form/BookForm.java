package jpabook.jpashop.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm extends ItemForm {

    private String author;
    private String isbn;

}
