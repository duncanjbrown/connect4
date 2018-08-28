RESOURCES_DIR = resources/public
TARGET_DIR = target/build

build: ${TARGET_DIR}/connect4.js ${TARGET_DIR}/css/style.css ${TARGET_DIR}/index.html

${TARGET_DIR}/connect4.js:
	clojure -m figwheel.main -bo prod

${TARGET_DIR}/css/style.css: ${RESOURCES_DIR}/css/style.css
	mkdir -p $(@D)
	cp $^ $@

${TARGET_DIR}/index.html: ${RESOURCES_DIR}/index.html
	sed "s/cljs-out\/dev-main.js/connect4.js/" < $^ > $@

clean:
	-@rm ${TARGET_DIR}/connect4.js
	-@rm ${TARGET_DIR}/index.html
	-@rm -r ${TARGET_DIR}/css

