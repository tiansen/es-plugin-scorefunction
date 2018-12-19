package com.ferraborghini.function;

import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.Explanation;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.LeafScoreFunction;
import org.elasticsearch.common.lucene.search.function.ScoreFunction;

import java.io.IOException;

public class TestScoreFunction extends ScoreFunction {

    private String fieldName;
    private float[] fieldValue;

    TestScoreFunction(String fieldName, String fieldValue) {
        super(CombineFunction.REPLACE);
        this.fieldName = fieldName;
        this.fieldValue = new float[100];
    }

    @Override
    public LeafScoreFunction getLeafScoreFunction(LeafReaderContext leafReaderContext) throws IOException {
        BinaryDocValues docValues = leafReaderContext.reader().getBinaryDocValues(fieldName);

        return new LeafScoreFunction() {
            @Override
            public double score(int docId, float subScore) throws IOException {
                float score = 0f;
                if (!docValues.advanceExact(docId)) {
                    throw new IllegalArgumentException("field not found");
                }
                byte[] storedValue = docValues.binaryValue().bytes;

                return score;
            }

            @Override
            public Explanation explainScore(int i, Explanation explanation) throws IOException {
                return Explanation.match(0f, "test");
            }
        };
    }

    @Override
    public boolean needsScores() {
        return true;
    }

    @Override
    protected boolean doEquals(ScoreFunction scoreFunction) {
        return false;
    }

    @Override
    protected int doHashCode() {
        return 0;
    }
}
