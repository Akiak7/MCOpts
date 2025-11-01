package ivorius.mcopts.commands.parameters;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ParametersParseTest
{
    @Test
    public void parsesQuotedAndUnquotedArgumentsConsistently()
    {
        String[] args = {"foo", "\"bar baz\"", "qux", "\"\"", ""};

        List<Pair<String, String>> parsed = Parameters.parse(args).collect(Collectors.toList());

        assertEquals(5, parsed.size());
        assertEquals(Pair.of("foo", "foo"), parsed.get(0));
        assertEquals(Pair.of(" \"bar baz\"", "bar baz"), parsed.get(1));
        assertEquals(Pair.of(" qux", "qux"), parsed.get(2));
        assertEquals(Pair.of(" \"\"", ""), parsed.get(3));
        assertEquals(Pair.of("", ""), parsed.get(4));
    }
}
