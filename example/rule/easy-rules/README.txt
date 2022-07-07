easy-rules 目前支持三种组合模式的rule:
1. UnitRuleGroup
只要一个不符合, 就都不执行了, 就是要么都执行, 要么都不执行

2. ActivationRuleGroup
选择第一个, 其他的就不执行了

3. ConditionalRuleGroup
找到优先级最高的, 如果符合, 然后找到其他的符合的rule并执行