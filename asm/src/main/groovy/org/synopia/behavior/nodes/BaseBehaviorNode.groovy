package org.synopia.behavior.nodes

import org.synopia.behavior.BehaviorNode
import org.synopia.behavior.GlobalVariable
import org.synopia.behavior.Visitor
import org.synopia.behavior.generators.BTMethodGenerator
import org.synopia.behavior.tree.BehaviorAction

/**
 * Created by synopia on 17.07.2014.
 */
abstract class BaseBehaviorNode implements BehaviorNode<BehaviorNode> {
    BehaviorNode parent
    int id = 0
    Map debug
    int memoryOffset
    int localsSize
    String command = ""

    List<GlobalVariable> locals = []
    Map<String, GlobalVariable> mapping = [:]

    String name() {
        this.class.name
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append(name())
        sb.append("@")
        sb.append(memoryOffset)
        sb.append(" [")
        locals.each { loc ->
            sb.append(loc.name)
            sb.append(":")
            sb.append(loc.size)
            sb.append("@")
            sb.append(loc.memoryPosition())
        }
        sb.append("] ")
        sb.toString()
    }

    def allocate(String name, def value) {
        def var = new GlobalVariable(this, name, localsSize, value)
        if (var.valid()) {
            locals << var
            mapping[name] = var
            localsSize += var.size
        } else {
            throw new IllegalArgumentException("Cannot allocate memory for $name")
        }
    }

    int size() {
        localsSize
    }

    GlobalVariable find(String name) {
        def v = mapping[name]
        if (v == null) {
            throw new IllegalArgumentException("$name is not defined")
        }
        return v
    }

    boolean has(String name) {
        return mapping[name] != null
    }


    Map defaults() {
        [debug: [execute: true]]
    }

    public void setParent(BehaviorNode parent) {
        this.parent = parent
    }

    Map setAttributes(Map<String, Object> map) {
        def d = deepMerge(defaults(), map)
        if (d["id"]) {
            id = (int) d["id"]
        }
        if (d["command"]) {
            command = d['command'];
        }
        if (d["locals"]) {
            d["locals"].each { Map hash ->
                hash.each { String key, def value ->
                    allocate(key, value)
                }
            }
        }

        debug = d["debug"]
        return d
    }

    boolean debug(BehaviorAction action) {
        if (debug && debug[action.toString().toLowerCase()] != null) {
            return debug[action.toString().toLowerCase()]
        }
        return parent && parent.debug(action)
    }

    void assembleSetup() {

    }

    void assembleTearDown() {

    }

    void assembleConstruct(BTMethodGenerator gen) {
        locals.each { local ->
            gen.store(local) {
                gen.push(local.constructValue)
            }

        }
    }

    void assembleExecute(BTMethodGenerator gen) {

    }

    void assembleDestruct(BTMethodGenerator gen) {
    }

    static Map deepMerge(Map self, Map hash) {
        Map target = self.clone()
        hash.each { key, value ->
            if ((value instanceof Map) && self[key] != null && (self[key] instanceof Map)) {
                target[key] = deepMerge(target[key], hash[key])
            } else {
                target[key] = hash[key]
            }
        }
        return target
    }

    @Override
    void insertChild(int index, BehaviorNode child) {
        throw new IllegalStateException("Not allowed");
    }

    @Override
    void replaceChild(int index, BehaviorNode child) {
        throw new IllegalStateException("Not allowed");
    }

    @Override
    BehaviorNode removeChild(int index) {
        throw new IllegalStateException("Not allowed");
    }

    @Override
    BehaviorNode getChild(int index) {
        throw new IllegalStateException("Not allowed");
    }

    @Override
    int getChildrenCount() {
        return 0
    }

    @Override
    int getMaxChildren() {
        return 0
    }

    @Override
    def <T> T visit(T item, Visitor<T> visitor) {
        return visitor.visit(item, this);
    }
}
