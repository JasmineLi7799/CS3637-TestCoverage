package edu.utdallas;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


class MethodTransformVisitor extends MethodVisitor implements Opcodes {
	String mName, className;
	//int line, executedline;

	public MethodTransformVisitor(final MethodVisitor mv, String className, String name) {
		super(ASM5, mv);
		this.mName = name;
		this.className = className;
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		//---------
		mv.visitLdcInsn(className.replace('/', '.') + ":" + line);
		mv.visitMethodInsn(INVOKESTATIC, "edu/utdallas/util/Helper", "addExecutedLine", "(Ljava/lang/String;)V", false);
		//---------
		super.visitLineNumber(line, start);
	}
}
