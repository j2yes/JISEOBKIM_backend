package com.jskim.banking.util.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursorPageQuery<T> {

  private T cursor;
  @Min(1)
  @Max(100)
  @NotNull
  private Integer size;
}
