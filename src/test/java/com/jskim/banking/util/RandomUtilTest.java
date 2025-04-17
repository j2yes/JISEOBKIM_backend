package com.jskim.banking.util;

import com.jskim.banking.util.random.RandomUtil;
import java.util.HashSet;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class RandomUtilTest {

  @Test
  void 랜덤_문자열_생성_테스트() {
    // when
    String result = RandomUtil.generateRandomDigit(13);

    // then
    assertThat(result).hasSize(13);
  }

  @Test
  void 랜덤_문자열_숫자로만_구성되었는지_테스트() {
    // when
    String result = RandomUtil.generateRandomDigit(13);

    // then
    assertThat(result).matches("\\d{13}");
  }

  @RepeatedTest(10)
  void 랜덤_문자열_유니크_정도_비교_테스트() {
    Set<String> generated = new HashSet<>();

    for (int i = 0; i < 10000; i++) {
      generated.add(RandomUtil.generateRandomDigit(13));
    }

    assertThat(generated).hasSizeGreaterThan(9995);
  }
}