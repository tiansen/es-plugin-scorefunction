package com.ferraborghini.function;

import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.lucene.search.function.ScoreFunction;
import org.elasticsearch.common.xcontent.ConstructingObjectParser;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.QueryShardContext;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;

import java.io.IOException;

public class TestFunctionBuilder extends ScoreFunctionBuilder<TestFunctionBuilder> {
    private static final ParseField FIELD = new ParseField("field");
    private static final ParseField VALUE = new ParseField("value");
    public static final String NAME = "test_func";
    private static final ConstructingObjectParser<TestFunctionBuilder, Void> PARSER = new ConstructingObjectParser<>(
            NAME, args -> new TestFunctionBuilder((String) args[0], args[1])
    );

    static {
        PARSER.declareString(ConstructingObjectParser.constructorArg(), FIELD);
        PARSER.declareString(ConstructingObjectParser.constructorArg(), VALUE);
    }

    private String fieldName;
    private String fieldValue;
    private String functionName;
    private TestScoreFunction testScoreFunction;

    private TestFunctionBuilder(String fieldName, Object inputValue) {
        this.fieldName = fieldName;
        this.fieldValue = (String) inputValue;
        this.testScoreFunction = new TestScoreFunction(this.fieldName, this.fieldValue);
    }

    public TestFunctionBuilder(StreamInput in) throws IOException {
        super(in);
        functionName = in.readString();
        fieldName = in.readString();
        fieldValue = in.readString();
        this.testScoreFunction = new TestScoreFunction(this.fieldName, this.fieldValue);
    }

    public static TestFunctionBuilder fromXContent(XContentParser parser) {
        return PARSER.apply(parser, null);

    }

    @Override
    protected void doWriteTo(StreamOutput out) throws IOException {
        out.writeString(functionName);
        out.writeString(fieldName);
        out.writeString(fieldValue);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected void doXContent(XContentBuilder xContentBuilder, Params params) throws IOException {
        xContentBuilder.startObject(functionName);
        xContentBuilder.field(FIELD.getPreferredName(), fieldName);
        xContentBuilder.field(VALUE.getPreferredName(), fieldValue);
        xContentBuilder.endObject();
    }

    @Override
    protected boolean doEquals(TestFunctionBuilder testFunctionBuilder) {
        return false;
    }


    @Override
    protected int doHashCode() {
        return 0;
    }

    @Override
    protected ScoreFunction doToFunction(QueryShardContext queryShardContext) throws IOException {
        return testScoreFunction;
    }
}
