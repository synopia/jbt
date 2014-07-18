import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.util.ASMifier
import org.objectweb.asm.util.TraceClassVisitor
import org.synopia.behavior.Assembler
import org.synopia.behavior.BTreeBuilder
import org.synopia.behavior.commands.Dispatcher
import org.synopia.behavior.generators.OptimizeJumpTransformer

/**
 * Created by synopia on 15.07.2014.
 */

def cli = new CliBuilder(usage: 'jbtc [options] <files>', header: 'Options:')
cli.help('print this message')
cli.o(longOpt: 'out', args: 1, argName: 'format', 'set output format, defaults to bin)')
cli.r(longOpt: 'run', args: 1, argName: 'times', 'runs the generated tree')
def options = cli.parse(args)

def outFormat = options.o
if (!outFormat) {
    outFormat = "bin"
}
int runs = 0
if (options.r) {
    runs = Integer.parseInt(options.r)
}

if (options.arguments().size() == 0) {
    println "No source files given!"
    cli.usage()
    System.exit(-1)
}

options.arguments().each { filename ->
    def builder = new BTreeBuilder();

    def binding = new Binding(builder: builder)
    def shell = new GroovyShell(this.class.classLoader, binding)
    def assembler = new Assembler()
    def tree = assembler.assemble(shell.evaluate(new File(filename)))

    def bytecode = assembler.getBytecode()

    println(filename)
    println "  local variables: ${assembler.maxLocalCounter}"
    println "  memory:          ${tree.size()} bytes"
    println "  class code:      ${bytecode.length} bytes"

    run(assembler, runs)

    ClassReader r = new ClassReader(bytecode)

    def cl = new ClassNode()
    r.accept(cl, 0)

    cl.methods.each {
        def transformer = new OptimizeJumpTransformer(null)
        transformer.transform(it)

        Analyzer a = new Analyzer(new BasicInterpreter());
        a.analyze("x", it)
        def frames = a.getFrames()
        def insns = it.instructions.toArray()
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == null && !(insns[i] instanceof LabelNode)) {
                iy.instructions.remove(insns[i])
            }
        }
    }


    switch (outFormat) {
        case 'bin':
            File classFile = new File(filename.substring(0, filename.lastIndexOf('.')) + ".class")
            classFile.withOutputStream { out ->
                out.write(bytecode)
            }
            break
        case 'asm':
            TraceClassVisitor visitor = new TraceClassVisitor(new PrintWriter(System.out))
            reader = new ClassReader(bytecode);
            reader.accept(visitor, 0)
            break
        case 'gen':
            ClassReader reader = new ClassReader(bytecode);
            reader.accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out)), 0);
            break
    }

}

def run(Assembler assembler, int count) {
    if (count == 0) {
        return
    }
    def dispatcher = new Dispatcher(assembler.createInstance())
    print("running")
    count.times {
//    while(true) {
        println(dispatcher.run())
//        sleep(10)
    }
    println()
}




