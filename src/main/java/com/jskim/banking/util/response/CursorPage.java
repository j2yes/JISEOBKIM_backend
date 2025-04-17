package com.jskim.banking.util.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CursorPage<T, C> {

  private List<T> data;
  private C nextCursor;
  private boolean hasNext;
}
