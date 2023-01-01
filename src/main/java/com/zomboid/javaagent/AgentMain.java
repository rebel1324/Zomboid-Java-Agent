package com.zomboid.javaagent;


import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * Description:
 *
 * @author liuzhengyang
 * @version 1.0
 * @since 2017-03-15
 */
public class AgentMain {
	public static void premain(String agentOps, Instrumentation inst) {
		instrument(agentOps, inst);
	}

	public static void agentmain(String agentOps, Instrumentation inst) {
//		instrument(agentOps, inst);
	}

	/**
	 * agentOps is aop target classname
	 */
	private static void instrument(String agentOps, Instrumentation inst) {
		System.out.println("====================================================================================================FUCKME");
		System.out.println(agentOps);
		System.out.println(inst);
		System.out.println("====================================================================================================FUCKME");
		try {
			inst.addTransformer(new AOPTransformer(agentOps));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

