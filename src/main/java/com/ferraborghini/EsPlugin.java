package com.ferraborghini;

import com.ferraborghini.function.TestFunctionBuilder;
import org.elasticsearch.plugins.IngestPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;

import java.util.ArrayList;
import java.util.List;

public class EsPlugin extends Plugin implements SearchPlugin, IngestPlugin {

    /**
     * 插件注册
     * 启动之后，es会加载插件
     */
    @Override
    public List<ScoreFunctionSpec<?>> getScoreFunctions() {
        List<ScoreFunctionSpec<?>> functionsList = new ArrayList<>();

        functionsList.add(new ScoreFunctionSpec<>(
                TestFunctionBuilder.NAME,
                TestFunctionBuilder::new,
                TestFunctionBuilder::fromXContent

        ));
        return functionsList;
    }
}
