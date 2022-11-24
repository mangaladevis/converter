package com.bubbles.learning.converter;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Unit test for simple Parser.
 */
class DatToJsonConverterTest {

	@Test
	public void ifInputHasNoComma() {
		JSONAssert.assertEquals("[{\"a\":\"b\"}]", DatToJsonConverter.convert(Collections.singletonList("a=b")), true);
	}

	@Test
	public void ifInputHasCommas() {
		JSONAssert.assertEquals("[{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"}]",
				DatToJsonConverter.convert(Collections.singletonList("a=b,c=d,e=f")), true);
	}

	@Test
	public void ifInputHasSquareBracket() {
		JSONAssert.assertEquals("[{\"a\":\"b\",\"c\":{\"d\":\"d1\"}}]",
				DatToJsonConverter.convert(Collections.singletonList("a=b,c=[d=d1]")), true);
	}

	@Test
	public void ifInputHasExtraClosingSquareBracket() {
		JSONAssert.assertEquals("[{\"a\":\"b\",\"c\":{\"d\":\"d1]\"}}]",
				DatToJsonConverter.convert(Collections.singletonList("a=b,c=[d=d1]]")), true);
	}

	@Test
	public void ifInputHasMultipleKeyValuePairsInSquareBracket() {
		JSONAssert.assertEquals("[{\"a\":\"b\",\"c\":{\"d\":\"d1\",\"e\":\"e1\",\"f\":\"f1\"}}]",
				DatToJsonConverter.convert(Collections.singletonList("a=b,c=[d=d1,e=e1,f=f1]")), true);
	}

	@Test
	public void ifInputHasMultipleNestedSquareBrackets() {
		JSONAssert.assertEquals(
				"[{\"a\":\"b\",\"c\":{\"d\":\"d1\",\"e\":\"e1\",\"f\":{\"g\":{\"h\":\"h3\",\"i\":\"i3\"},\"j\":\"j2\"}},\"k\":\"l\"}]",
				DatToJsonConverter.convert(Collections.singletonList("a=b,c=[d=d1,e=e1,f=[g=[h=h3,i=i3],j=j2]],k=l")),
				true);
	}
}
