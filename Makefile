.PHONY: clean all

JAVAC = javac
JFLAGS = -g
BIN = bin
SOURCE = src
CLASSES := $(wildcard $(SOURCE)/*.java)
CLASS_FILES := $(patsubst $(SOURCE)/%.java,$(BIN)/%.class,$(CLASSES))

all: $(CLASS_FILES)

$(BIN)/%.class: $(SOURCE)/%.java
	@mkdir -p $(BIN)
	$(JAVAC) $(JFLAGS) -cp $(BIN) $< -d $(BIN)

clean:
	@rm -rf $(BIN)
