package hu.oe.yokudlela.food.generated.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Food
 */
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.10.0")
public class Food implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;

  private String name;

  private Integer categoryId;

  private Integer price;

}

