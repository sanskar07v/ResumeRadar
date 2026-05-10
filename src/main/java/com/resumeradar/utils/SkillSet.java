package com.resumeradar.utils;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Getter
public class SkillSet {

    private static final Set<String> skillSet;

    static {
        String[] skillsArray = {
                "java", "c", "cpp", "python", "js", "html", "css", "javascript",
                "data structure and algorithm", "dsa", "oop", "object oriented programming",
                "version control", "git", "github", "sql", "dbms", "problem solving",
                "debugging", "team collaboration", "reactjs", "react.js", "angular",
                "vue.js", "vuejs", "bootstrap", "grid", "flexbox", "nodejs", "node.js",
                "mysql", "mongodb", "oracle", "restful apis", "ui", "ux", "ui/ux",
                "tcp/ip", "tcp", "ip", "dns", "firewalls", "wireshark", "kali linux",
                "metasploit", "linux", "windows", "aws", "azure", "google cloud", "cloud",
                "ci/cd", "ci", "cd", "r", "pandas", "numpy", "tableau", "powerbi", "power bi",
                "matplot", "machine learning", "ml", "ai", "testing", "selenium", "junit",
                "testng", "jira", "scikit-learn", "tensorflow", "pytorch", "linear algebra",
                "probability", "statistics", "os", "operating system", "ruby", "spring",
                "springboot", "spring security", "nosql", "json", "xml", "jwt", "oauth",
                "django", "flask", "expressjs", "express.js", "docker", "apache",
                "microservice", "artificial intelligence"
        };

        // Use thread-safe set
        skillSet = new CopyOnWriteArraySet<>(Arrays.asList(skillsArray));
    }

    public static Set<String> addSkill(String skill) {
        skillSet.add(skill.trim().toLowerCase());
        return skillSet;
    }

    public static Set<String> getSkillSet() {
        return Collections.unmodifiableSet(skillSet);
    }
}
