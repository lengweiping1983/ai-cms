;(function ( $, window, document, undefined ) {

	// VARIABLES
	var selected_drawable;
	var canvas;
	var active_object;
	var drawables = new Array();

	var canvas_defaults = {
        frameWidth : 1280,
        frameHeight : 720,
        maxZoom : "auto",
        navigator : false,
        navigatorImagePreview : false,
        fullscreen : false
    }

	var annotation_defaults = {
        tint_color : "#ffff33",
        style : 4,
        popup_width : "auto",
        popup_height : "auto",
        popup_position : "top",
        content_type : "text", // or "custom-html"
        title : "未关联元素",
        text : "双击关联元素",
        text_color : "#000000",
        html : "",
        id : "my-annotation-",
        spot_left : 0,
        spot_top : 0,
        spot_width : 44,
        spot_height : 44,
        spot_circle : true
    };

	// CLASSES
	function NDD_Drawable_Canvas(obj, width, height, cb) {
		this.obj = $(obj);
		this.obj_img = undefined;
		this.img = new Image();
		this.width = width;
		this.height = height;

		this.obj_drawables_container = this.obj.find('.ndd-drawables-container');

		this.is_drawing = false;
		this.obj_temp = undefined;

		this.event_initial_x = 0;
		this.event_initial_y = 0;

		this.temp_pos_x = 0;
		this.temp_pos_y = 0;
		this.temp_width = 0;
		this.temp_height = 0;

		this.settings = $.extend({}, canvas_defaults);

		// events
		this.init(cb);
	}
	NDD_Drawable_Canvas.prototype = {
		init : function(cb) {
			var self = this;

			canvas = self;
			self.obj_img = self.obj.find('img');

			if (self.width != 0 && self.height != 0) {
				self.obj.css({
					width : self.width,
					height : self.height
				});

				self.img.src = self.obj_img.attr('src');

				cb();
			} else {
				self.img.onload = function() {
					self.width = self.img.width;
					self.height = self.img.height;

					/*
					if (self.width > $('#panel-canvas').width()) {
						var scale = self.width / $('#panel-canvas').width();

						self.width = $('#panel-canvas').width();
						self.height = self.height / scale;
					}

					if (self.height > $('#panel-canvas').height()) {
						var scale = self.height / $('#panel-canvas').height();

						self.height = $('#panel-canvas').height();
						self.width = self.width / scale;
					}
					*/

					self.obj.css({
						width : self.width,
						height : self.height
					});

					cb();
				}

				self.img.src = self.obj_img.attr('src');
			}
		},
		handle_event : function(e) {
			var self = this;
			
			var editType = $("#editType").val();

			if (e.type == "mousedown") {

			}

			if (e.type == "mousemove" && editType && editType != 'uri') {
				if (!self.is_drawing) {
					self.is_drawing = true;
					self.start_drawing(e.pageX, e.pageY);
				}

				if (self.is_drawing) {
					self.draw(e.pageX, e.pageY);
				}
			}

			if (e.type == "mouseup" && editType && editType != 'uri') {
				if (self.is_drawing) {
					self.is_drawing = false;
					self.stop_drawing();
				} else if ($(e.target).hasClass('ndd-drawable-canvas') || $(e.target).hasClass('ndd-drawable-canvas-image')) {
					var x = e.pageX - self.obj.offset().left;
					var y = e.pageY - self.obj.offset().top;

					self.create_circle_spot(x, y);
				}
			}
		},
		start_drawing : function(pageX, pageY) {
			var self = this;

			self.obj_drawables_container.append('<div class="ndd-drawable-rect ndd-spot-rect ndd-rect-style-'+ annotation_defaults.style +'" id="temp"><div class="ndd-icon-main-element" style="background-color: rgba(255, 255, 51, 0.2);"></div><div class="ndd-icon-border-element" style="border-color: rgb(255, 255, 51);"></div></div>');
			self.obj_temp = $('#temp');

			self.temp_pos_x = pageX - self.obj.offset().left;
			self.temp_pos_y = pageY - self.obj.offset().top;

			self.obj_temp.css({
				"left" : self.temp_pos_x,
				"top" : self.temp_pos_y,
				"width" : 0,
				"height" : 0
			});

			self.event_initial_x = pageX;
			self.event_initial_y = pageY;
		},
		draw : function(pageX, pageY) {
			var self = this;

			self.temp_width = pageX - self.event_initial_x;
			self.temp_height = pageY - self.event_initial_y;

			if (self.temp_pos_x + self.temp_width > self.width) {
				self.temp_width = self.width - self.temp_pos_x;
			}

			if (self.temp_pos_y + self.temp_height > self.height) {
				self.temp_height = self.height - self.temp_pos_y;
			}

			self.obj_temp.css({
				"width" : self.temp_width,
				"height" : self.temp_height
			});
		},
		stop_drawing : function() {
			var self = this;

			var x = self.obj_temp.offset().left - self.obj.offset().left;
			var y = self.obj_temp.offset().top - self.obj.offset().top;
			var width = (self.obj_temp.width() < 44) ? 44 : self.obj_temp.width();
			var height = (self.obj_temp.height() < 44) ? 44 : self.obj_temp.height();

			if (width == 44 && height == 44) {
				self.create_circle_spot(x, y);
			} else {
				self.create_rect_spot(x, y, width, height);
			}

			self.obj_temp.remove();
			self.obj_temp = undefined;
		},
		apply_settings : function(new_settings) {
			var self = this;

			self.settings = new_settings;
		},
		create_circle_spot : function(x, y) {
			var self = this;

			var drawable = new NDD_Drawable(x, y, self.obj_drawables_container, self);
			return drawable;
		},
		create_rect_spot : function(x, y, width, height) {
			var self = this;

			var drawable_rect = new NDD_Drawable_Rect(x, y, width, height, self.obj_drawables_container, self);
			return drawable_rect;
		}
	};

	function NDD_Drawable(x, y, obj_parent, canvas) {
		this.is_rect = false;

		this.canvas = canvas;
		this.obj_parent = obj_parent;
		this.obj_visible = undefined;
		this.obj_active_area = undefined;
		this.obj = undefined;
		this.id = generate_annotation_id();
		this.x = x;
		this.y = y;

		this.width = 44;
		this.height = 44;
		this.left = x - this.width/2;
		this.top = y - this.height/2;

		this.is_selected = false;
		this.is_moving = false;

		// moving
		this.event_initial_x = 0;
		this.event_initial_y = 0;
		this.initial_left = 0;
		this.initial_top = 0;

		// annotation
		this.annotation = undefined;

		this.settings = $.extend({}, annotation_defaults);
		this.settings.id = this.id;
		this.settings.spot_left = this.left;
		this.settings.spot_top = this.top;
		this.settings.spot_width = this.width;
		this.settings.spot_height = this.height;
		this.settings.spot_circle = true;

		this.init();
	}
	NDD_Drawable.prototype = {
		init : function() {
			var self = this;

			drawables[self.id] = self;

			self.obj_parent.append('<div class="ndd-drawable" id="' +self.id+ '"></div>');
			self.obj = $('#' + self.id);

			self.obj.append('<div class="ndd-spot-icon icon-style-'+ self.settings.style +'"><div class="ndd-icon-main-element"></div><div class="ndd-icon-border-element"></div></div>');
			self.obj_visible = self.obj.find('.ndd-spot-icon');

			self.obj.append('<div class="ndd-drawable-active-area"></div>');
			self.obj_active_area = self.obj.find('.ndd-drawable-active-area');

			self.obj.append('<div class="ndd-drawable-marquee"></div>');

			self.obj_visible.find('.ndd-icon-main-element').css({
				"background-color" : 'rgba('+ hexToRgb(self.settings.tint_color).r +', '+ hexToRgb(self.settings.tint_color).g +', '+ hexToRgb(self.settings.tint_color).b +', 0.2)'
			});

			self.obj_visible.find('.ndd-icon-border-element').css({
				"border-color" : self.settings.tint_color
			});
			
			self.constrain_position();

			self.obj.css({
				left : self.left,
				top : self.top,
				width : self.width,
				height : self.height
			});

			// annotation
			self.annotation = new NDD_Annotation(self.obj, self);
		},
		handle_event : function(e) {
			var self = this;
			var editType = $("#editType").val();
			if (e.type == "mousedown") {

			}

			if (e.type == "mousemove" && editType && editType != 'uri') {
				if (!self.is_moving) {
					self.is_moving = true;

					if (!self.is_selected) {
						self.select(false);
					}

					self.start_moving(e.pageX, e.pageY);
				}
				if (self.is_moving) {
					self.move(e.pageX, e.pageY);
				}
			}

			if (e.type == "mouseup") {
				if (self.is_moving) {
					self.is_moving = false;
					self.end_moving();
					
					var json = {};
					
					json.positionId = self.settings.id;
					json.position = self.calculate_position();
					
					if (editType && editType == 'template') {
						$.TemplateParamController.editByPositionFromAnnotator(json);
					} else {
						$.AlbumItemController.editByPositionFromAnnotator(json);
					}
					
				} else {
					self.select(false);
				}
			}

			if (e.type == "dblclick") {
				self.select(true);
				
				if (editType && editType == 'uri') {
					$.UriController.toEditByPositionFromAnnotator(self.settings.id);
				} else if (editType && editType == 'template') {
					$.TemplateParamController.toEditByPositionFromAnnotator(self.settings.id);
				} else {
					$.AlbumItemController.toEditByPositionFromAnnotator(self.settings.id);
				}
			}
		},
		
		select : function(needSelect) {
			var self = this;
			apply_settings();

			if (!needSelect && self.is_selected) {
				// deselect
				self.obj.removeClass('ndd-selected');
				self.is_selected = false;
				selected_drawable = undefined;

				self.annotation.hide();
				refresh_form();
			} else {
				// select
				if (selected_drawable != undefined) {
					selected_drawable.select();
				}

				self.obj.addClass('ndd-selected');
				self.is_selected = true;
				selected_drawable = self;

				self.annotation.show();
				load_settings(self.settings);
				
				self.calculate_position();
			}
		},
		
		calculate_position : function() {
			var self = this;
			
			var spot_circle = true;
			log(self.left);
			log(self.top);
			
			var spot_left = toFixed((self.left + 22)/ canvas.width * 100, 2) + '%';
			var spot_top = toFixed((self.top + 22) / canvas.height * 100, 2) + '%';
			var spot_width = toFixed(self.width / canvas.width * 100, 2) + '%';
			var spot_height = toFixed(self.height / canvas.width * 100, 2) + '%';
			
			var json = {};
			json.id = self.settings.id;
			json.spot_left = spot_left;
			json.spot_top = spot_top;
			json.spot_width = spot_width;
			json.spot_height = spot_height;
			json.spot_circle = spot_circle;
			json.title = self.settings.title;
			json.text = self.settings.text;
			
			var jsonStr = JSON.stringify(json);
			$('#input-position').val(jsonStr);
			
			return jsonStr;
		},
		
		start_moving : function(pageX, pageY) {
			var self = this;

			self.event_initial_x = pageX;
			self.event_initial_y = pageY;
			self.initial_left = self.left;
			self.initial_top = self.top;

			self.obj.addClass("ndd-moving");
		},
		move : function(pageX, pageY) {
			var self = this;

			var dx = pageX - self.event_initial_x;
			var dy = pageY - self.event_initial_y;

			self.left = self.initial_left + dx;
			self.top = self.initial_top + dy;

			self.constrain_position();
			self.redraw();
			
			if (self.is_selected) {
				self.calculate_position();
			}
		},
		end_moving : function() {
			var self = this;

			self.obj.removeClass("ndd-moving");
		},
		constrain_position : function() {
			var self = this;

			if (self.left > self.canvas.width - self.width) {
				self.left = self.canvas.width - self.width;
			}

			if (self.left < 0) {
				self.left = 0;
			}

			if (self.top > self.canvas.height - self.height) {
				self.top = self.canvas.height - self.height;
			}

			if (self.top < 0) {
				self.top = 0;
			}
		},
		redraw : function() {
			var self = this;

			self.obj.css({
				left : self.left,
				top : self.top
			});
		},
		apply_settings : function(new_settings) {
			var self = this;

			self.settings = new_settings;

			self.obj_visible.find('.ndd-icon-main-element').css({
				"background-color" : self.settings.tint_color
			});

			self.obj_visible.find('.ndd-icon-border-element').css({
				"border-color" : self.settings.tint_color
			});

			self.settings.spot_circle = true;
			self.settings.spot_left = self.left;
			self.settings.spot_top = self.top;
			self.settings.spot_width = self.width;
			self.settings.spot_height = self.height;

			// style
			self.obj_visible.removeClass('icon-style-0');
			self.obj_visible.removeClass('icon-style-1');
			self.obj_visible.removeClass('icon-style-2');
			self.obj_visible.removeClass('icon-style-3');
			self.obj_visible.removeClass('icon-style-4');
			self.obj_visible.removeClass('icon-style-5');
			self.obj_visible.removeClass('icon-style-6');
			self.obj_visible.removeClass('icon-style-7');
			self.obj_visible.removeClass('icon-style-8');
			self.obj_visible.removeClass('icon-style-9');
			self.obj_visible.removeClass('icon-style-10');
			self.obj_visible.removeClass('icon-style-11');
			self.obj_visible.removeClass('icon-style-12');
			self.obj_visible.removeClass('icon-style-13');

			self.obj_visible.addClass('icon-style-' + self.settings.style);
			self.obj_visible.find('img').remove();

			if (self.settings.style > 4) {
				var i = self.settings.style - 4;
				self.obj_visible.append('<img src="icon_loc_0'+ i +'.png">');
			}
		}
	};

	function NDD_Drawable_Rect(x, y, width, height, obj_parent, canvas) {
		this.is_rect = true;

		this.canvas = canvas;
		this.obj_parent = obj_parent;
		this.obj = undefined;
		this.obj_visible = undefined;
		this.obj_visible_main_element = undefined;
		this.id = generate_annotation_id();
		this.x = x;
		this.y = y;

		this.width = width;
		this.height = height;

		this.is_selected = false;
		this.is_moving = false;
		this.is_scaling = false;

		// moving
		this.event_initial_x = 0;
		this.event_initial_y = 0;
		this.initial_left = 0;
		this.initial_top = 0;

		// scaling
		this.initial_width = 0;
		this.initial_height = 0;
		this.scale_amount_x = 0;
		this.scale_amount_y = 0;

		// annotation
		this.annotation = undefined;

		this.settings = $.extend({}, annotation_defaults);
		this.settings.id = this.id;
		this.settings.spot_left = this.x;
		this.settings.spot_top = this.y;
		this.settings.spot_width = this.width;
		this.settings.spot_height = this.height;
		this.settings.spot_circle = false;

		this.init();
	}
	NDD_Drawable_Rect.prototype = {
		init : function() {
			var self = this;

			drawables[self.id] = self;

			self.obj_parent.append('<div class="ndd-drawable-rect" id="' +self.id+ '"></div>');
			self.obj = $('#' + self.id);

			self.obj.append('<div class="ndd-spot-rect ndd-rect-style-'+ self.settings.style +'"><div class="ndd-icon-main-element"></div><div class="ndd-icon-border-element"></div></div>');
			self.obj_visible = self.obj.find('.ndd-spot-rect');
			self.obj_visible_main_element = self.obj.find('.ndd-icon-main-element');
			self.obj.append('<div class="ndd-drawable-active-area"></div>');

			self.obj_visible.find('.ndd-icon-main-element').css({
				"background-color" : 'rgba('+ hexToRgb(self.settings.tint_color).r +', '+ hexToRgb(self.settings.tint_color).g +', '+ hexToRgb(self.settings.tint_color).b +', 0.2)'
			});

			self.obj_visible.find('.ndd-icon-border-element').css({
				"border-color" : self.settings.tint_color
			});
			
			self.constrain_position();

			self.obj.css({
				left : self.x,
				top : self.y,
				width : self.width,
				height : self.height
			});

			// handles
			self.obj.append('<div class="ndd-drawable-rect-handle-1 ndd-drawable-rect-handle"></div><div class="ndd-drawable-rect-handle-2 ndd-drawable-rect-handle"></div><div class="ndd-drawable-rect-handle-3 ndd-drawable-rect-handle"></div><div class="ndd-drawable-rect-handle-4 ndd-drawable-rect-handle"></div><div class="ndd-drawable-rect-handle-5 ndd-drawable-rect-handle"></div><div class="ndd-drawable-rect-handle-6 ndd-drawable-rect-handle"></div><div class="ndd-drawable-rect-handle-7 ndd-drawable-rect-handle"></div><div class="ndd-drawable-rect-handle-8 ndd-drawable-rect-handle"></div>');

			// annotation
			self.annotation = new NDD_Annotation(self.obj, self);
		},
		handle_event : function(e) {
			var self = this;
			var editType = $("#editType").val();
			if (e.type == "mousedown") {

			}

			if (e.type == "mousemove" && editType && editType != 'uri') {
				if ($(e.target).hasClass('ndd-drawable-rect-handle') && !self.is_scaling && !self.is_moving) {
					self.is_scaling = true;

					self.start_scaling(e.pageX, e.pageY, e);
				}

				if ($(e.target).hasClass('ndd-drawable-active-area') && !self.is_moving && !self.is_scaling) {
					self.is_moving = true;

					if (!self.is_selected) {
						self.select(false);
					}

					self.start_moving(e.pageX, e.pageY);
				}

				if (self.is_moving) {
					self.move(e.pageX, e.pageY);
				}

				if (self.is_scaling) {
					self.scale(e.pageX, e.pageY);
				}
			}

			if (e.type == "mouseup") {
				if (self.is_moving) {
					self.is_moving = false;
					self.end_moving();
					
					var json = {};
					
					json.positionId = self.settings.id;
					json.position = self.calculate_position();
					
					if (editType && editType == 'template') {
						$.TemplateParamController.editByPositionFromAnnotator(json);
					} else {
						$.AlbumItemController.editByPositionFromAnnotator(json);
					}
					
				} else if (self.is_scaling) {
					self.is_scaling = false;

					self.end_scaling();
					
					var json = {};
					
					json.positionId = self.settings.id;
					json.position = self.calculate_position();
					
					if (editType && editType == 'template') {
						$.TemplateParamController.editByPositionFromAnnotator(json);
					} else {
						$.AlbumItemController.editByPositionFromAnnotator(json);
					}
					
				} else {
					self.select(false);
				}
			}
			
			if (e.type == "dblclick") {
				self.select(true);
				
				if (editType && editType == 'uri') {
					$.UriController.toEditByPositionFromAnnotator(self.settings.id);
				} else if (editType && editType == 'template') {
					$.TemplateParamController.toEditByPositionFromAnnotator(self.settings.id);
				} else {
					$.AlbumItemController.toEditByPositionFromAnnotator(self.settings.id);
				}
			}
		},
		
		select : function(needSelect) {
			var self = this;
			apply_settings();

			if (!needSelect && self.is_selected) {
				// deselect
				self.obj.removeClass('ndd-selected');
				self.is_selected = false;
				selected_drawable = undefined;

				self.annotation.hide();
				refresh_form();
			} else {
				// select
				if (selected_drawable != undefined) {
					selected_drawable.select();
				}

				self.obj.addClass('ndd-selected');
				self.is_selected = true;
				selected_drawable = self;

				self.annotation.show();
				load_settings(self.settings);
				
				self.calculate_position();
			}
		},
		
		calculate_position : function() {
			var self = this;
			
			var spot_circle = false;
			var spot_left = toFixed(self.x / canvas.width * 100, 2) + '%';
			var spot_top = toFixed(self.y / canvas.height * 100, 2) + '%';
			var	spot_width = toFixed(self.width / canvas.width * 100, 2) + '%';
			var	spot_height = toFixed(self.height / canvas.height * 100, 2) + '%';
			
			var json = {};
			json.id = self.settings.id;
			json.spot_left = spot_left;
			json.spot_top = spot_top;
			json.spot_width = spot_width;
			json.spot_height = spot_height;
			json.spot_circle = spot_circle;
			json.title = self.settings.title;
			json.text = self.settings.text;
			
			var jsonStr = JSON.stringify(json);
			$('#input-position').val(jsonStr);
			
			return jsonStr;
		},
		
		start_scaling : function(pageX, pageY, event) {
			var self = this;

			self.event_initial_x = pageX;
			self.event_initial_y = pageY;
			self.initial_left = self.x;
			self.initial_top = self.y;
			self.initial_width = self.width;
			self.initial_height = self.height;

			if ($(event.target).hasClass("ndd-drawable-rect-handle-1")) {
				self.scale_amount_x = -1;
				self.scale_amount_y = -1;
			}

			if ($(event.target).hasClass("ndd-drawable-rect-handle-2")) {
				self.scale_amount_x = 0;
				self.scale_amount_y = -1;
			}

			if ($(event.target).hasClass("ndd-drawable-rect-handle-3")) {
				self.scale_amount_x = 1;
				self.scale_amount_y = -1;
			}

			if ($(event.target).hasClass("ndd-drawable-rect-handle-4")) {
				self.scale_amount_x = 1;
				self.scale_amount_y = 0;
			}

			if ($(event.target).hasClass("ndd-drawable-rect-handle-5")) {
				self.scale_amount_x = 1;
				self.scale_amount_y = 1;
			}

			if ($(event.target).hasClass("ndd-drawable-rect-handle-6")) {
				self.scale_amount_x = 0;
				self.scale_amount_y = 1;
			}

			if ($(event.target).hasClass("ndd-drawable-rect-handle-7")) {
				self.scale_amount_x = -1;
				self.scale_amount_y = 1;
			}

			if ($(event.target).hasClass("ndd-drawable-rect-handle-8")) {
				self.scale_amount_x = -1;
				self.scale_amount_y = 0;
			}
		},
		scale : function(pageX, pageY) {
			var self = this;

			var dx = pageX - self.event_initial_x;
			var dy = pageY - self.event_initial_y;

			dx *= self.scale_amount_x;
			dy *= self.scale_amount_y;

			// prevent negative scale
			if (self.width < self.initial_width) {
				if (Math.abs(dx) > self.initial_width - 44) {
					if (dx > 0) {
						dx = self.initial_width - 44;
					} else {
						dx = -self.initial_width + 44;
					}
				}
			}

			if (self.height < self.initial_height) {
				if (Math.abs(dy) > self.initial_height - 44) {
					if (dy > 0) {
						dy = self.initial_height - 44;
					} else {
						dy = -self.initial_height + 44;
					}
				}
			}

			// prevent going out of bounds (negative)
			if (dx > self.initial_left && self.scale_amount_x == -1) {
				dx = self.initial_left;
			}
			if (dy > self.initial_top && self.scale_amount_y == -1) {
				dy = self.initial_top;
			}

			// prevent going out of bounds (positive)
			if (dx > self.canvas.width - self.initial_left - self.initial_width && self.scale_amount_x == 1) {
				dx = self.canvas.width - self.initial_left - self.initial_width;
			}
			if (dy > self.canvas.height - self.initial_top - self.initial_height && self.scale_amount_y == 1) {
				dy = self.canvas.height - self.initial_top - self.initial_height;
			}

			// scaling direction
			if (self.scale_amount_x == -1) {
				self.x = self.initial_left - dx;
			}

			if (self.scale_amount_y == -1) {
				self.y = self.initial_top - dy;
			}

			// apply scale
			self.width = self.initial_width + dx;
			self.height = self.initial_height + dy;

			// positive scaling
			// if (self.width > self.canvas.width - self.x) {
			// 	self.width = self.canvas.width - self.x;
			// }

			// if (self.height > self.canvas.height - self.y) {
			// 	self.height = self.canvas.height - self.y;
			// }

			// redraw rect
			self.redraw();

			// redraw annotation
			self.annotation.initialize_dimentions();
		},
		end_scaling : function() {
			var self = this;
		},
		start_moving : function(pageX, pageY) {
			var self = this;

			self.event_initial_x = pageX;
			self.event_initial_y = pageY;
			self.initial_left = self.x;
			self.initial_top = self.y;

			self.obj.addClass("ndd-moving");
		},
		move : function(pageX, pageY) {
			var self = this;

			var dx = pageX - self.event_initial_x;
			var dy = pageY - self.event_initial_y;

			self.x = self.initial_left + dx;
			self.y = self.initial_top + dy;

			self.constrain_position();
			self.redraw();
			
			if (self.is_selected) {
				self.calculate_position();
			}
		},
		end_moving : function() {
			var self = this;

			self.obj.removeClass("ndd-moving");
		},
		constrain_position : function() {
			var self = this;

			if (self.x > self.canvas.width - self.width) {
				self.x = self.canvas.width - self.width;
			}

			if (self.x < 0) {
				self.x = 0;
			}

			if (self.y > self.canvas.height - self.height) {
				self.y = self.canvas.height - self.height;
			}

			if (self.y < 0) {
				self.y = 0;
			}
		},
		redraw : function() {
			var self = this;

			self.obj.css({
				width : self.width,
				height : self.height,
				left : self.x,
				top : self.y
			});

			if (self.settings.style == 1) {
				self.obj_visible_main_element.css({
					width : self.width,
					height : self.height
				});
			}

			if (self.settings.style == 2) {
				self.obj_visible_main_element.css({
					width : self.width - 10,
					height : self.height - 10
				});
			}

			if (self.settings.style == 3) {
				self.obj_visible_main_element.css({
					width : self.width - 6,
					height : self.height - 6
				});
			}

			if (self.settings.style == 4) {

			}
		},
		apply_settings : function(new_settings) {
			var self = this;

			self.settings = new_settings;

			self.obj_visible.find('.ndd-icon-main-element').css({
				"background-color" : 'rgba('+ hexToRgb(self.settings.tint_color).r +', '+ hexToRgb(self.settings.tint_color).g +', '+ hexToRgb(self.settings.tint_color).b +', 0.2)'
			});

			self.obj_visible.find('.ndd-icon-border-element').css({
				"border-color" : self.settings.tint_color
			});

			this.settings.spot_left = this.x;
			this.settings.spot_top = this.y;
			this.settings.spot_width = this.width;
			this.settings.spot_height = this.height;
			this.settings.spot_circle = false;

			// style
			self.obj_visible.removeClass('ndd-rect-style-0');
			self.obj_visible.removeClass('ndd-rect-style-1');
			self.obj_visible.removeClass('ndd-rect-style-2');
			self.obj_visible.removeClass('ndd-rect-style-3');
			self.obj_visible.removeClass('ndd-rect-style-4');

			self.obj_visible.addClass('ndd-rect-style-' + self.settings.style);

			self.redraw();
		}
	};

	function NDD_Annotation(parent, drawable) {
		this.obj_parent = parent;
		this.drawable = drawable;
		this.obj = undefined;
		this.obj_box = undefined;
		this.obj_arrow = undefined;
		this.obj_content = undefined;

		this.title = drawable.settings.title;
		this.text = drawable.settings.text;
		this.html = drawable.settings.html;
		this.content_type = drawable.settings.content_type;
		this.position = "top"; // top, bottom, left, right

		this.width = "auto";
		this.height = "auto";
		this.left = 0;
		this.top = 0;

		this.initialized_dimentions = false;
		this.is_visible = false;

		this.init();
	}
	NDD_Annotation.prototype = {
		init : function() {
			var self = this;

			self.obj_parent.append('<div class="ndd-annotation-container"></div>');
			self.obj = self.obj_parent.find('.ndd-annotation-container');

			self.obj.append('<div class="ndd-annotation-box" style="background-color: rgb(255, 255, 51);"></div>');
			// self.obj.append('<div class="ndd-annotation-arrow-up"></div>');

			self.obj_box = self.obj.find('.ndd-annotation-box');
			// self.obj_arrow = self.obj.find('.ndd-annotation-arrow-up');

			self.obj_box.append('<div class="ndd-annotation-content" style="color: rgb(0, 0, 0);"></div>');
			self.obj_box.append('<div class="ndd-annotation-arrow-down"></div>');

			self.obj_content = self.obj_box.find('.ndd-annotation-content');
			self.obj_arrow = self.obj_box.find('.ndd-annotation-arrow-down');

			// sample content
			self.obj_content.append('<h1 style="width:100px;color: rgb(0, 0, 0);">'+ self.title +'</h1><p style="width:100px;color: rgb(0, 0, 0);">'+ self.text +'</p>');
		},
		show : function() {
			var self = this;

			self.obj.addClass('ndd-annotation-visible');
			self.is_visible = true;

			if (!self.initialized_dimentions) {
				self.initialize_dimentions();
			}
		},
		hide : function() {
			var self = this;

			self.is_visible = false;

			self.obj.removeClass('ndd-annotation-visible');
		},
		initialize_dimentions : function() {
			var self = this;

			if (self.width == "auto") {
				self.obj_box.css({
					width : "auto"
				});

				self.width = self.obj_box.width();
			} else {
				self.obj_box.css({
					width : self.width
				});
			}

			if (self.height == "auto") {
				self.obj_box.css({
					height : "auto"
				});

				self.height = self.obj_box.height();
			} else {
				self.obj_box.css({
					height : self.height
				});
			}

			if (self.drawable.settings.popup_position == "top") {
				self.left = -self.width/2 + self.drawable.width/2;
				self.top = -self.height - 20;

				if (self.drawable.settings.style != 1 && self.drawable.settings.style != 2 && self.drawable.settings.style != 3 && self.drawable.settings.style != 4) {
					self.top -= 20;
				}

				self.obj_arrow.removeClass('ndd-annotation-arrow-up');
				self.obj_arrow.removeClass('ndd-annotation-arrow-left');
				self.obj_arrow.removeClass('ndd-annotation-arrow-right');
				self.obj_arrow.addClass('ndd-annotation-arrow-down');

				self.obj_arrow.css({
					left : self.width/2 - 10,
					top : "100%"
				});
			}

			if (self.drawable.settings.popup_position == "bottom") {
				self.left = -self.width/2 + self.drawable.width/2;
				self.top = self.drawable.height + 20;

				self.obj_arrow.removeClass('ndd-annotation-arrow-down');
				self.obj_arrow.removeClass('ndd-annotation-arrow-left');
				self.obj_arrow.removeClass('ndd-annotation-arrow-right');
				self.obj_arrow.addClass('ndd-annotation-arrow-up');

				self.obj_arrow.css({
					left : self.width/2 - 10,
					top : -10
				});
			}

			if (self.drawable.settings.popup_position == "left") {
				self.left = -self.width - 20;
				self.top = -self.height/2 + self.drawable.height/2;

				self.obj_arrow.removeClass('ndd-annotation-arrow-down');
				self.obj_arrow.removeClass('ndd-annotation-arrow-left');
				self.obj_arrow.removeClass('ndd-annotation-arrow-up');
				self.obj_arrow.addClass('ndd-annotation-arrow-right');

				self.obj_arrow.css({
					left : "100%",
					top : self.height/2 - 10
				});
			}

			if (self.drawable.settings.popup_position == "right") {
				self.left = self.drawable.width + 20;
				self.top = -self.height/2 + self.drawable.height/2;

				self.obj_arrow.removeClass('ndd-annotation-arrow-down');
				self.obj_arrow.removeClass('ndd-annotation-arrow-right');
				self.obj_arrow.removeClass('ndd-annotation-arrow-up');
				self.obj_arrow.addClass('ndd-annotation-arrow-left');

				self.obj_arrow.css({
					left : -10,
					top : self.height/2 - 10
				});
			}

			self.obj.css({
				left : self.left,
				top : self.top
			});
		},
		apply_settings : function(new_settings) {
			var self = this;

			// tint color
			self.obj_box.css({
				"background-color" : new_settings.tint_color
			});

			// width
			self.width = new_settings.popup_width;

			// height
			self.height = new_settings.popup_height;

			// popup position
			if (new_settings.popup_position == "top") {
				self.obj_arrow.css({
					"border-color" : 'transparent',
					"border-top-color" : new_settings.tint_color
				});
			}
			if (new_settings.popup_position == "bottom") {
				self.obj_arrow.css({
					"border-color" : 'transparent',
					"border-bottom-color" : new_settings.tint_color
				});
			}
			if (new_settings.popup_position == "left") {
				self.obj_arrow.css({
					"border-color" : 'transparent',
					"border-left-color" : new_settings.tint_color
				});
			}
			if (new_settings.popup_position == "right") {
				self.obj_arrow.css({
					"border-color" : 'transparent',
					"border-right-color" : new_settings.tint_color
				});
			}

			// content
			self.title = new_settings.title;
			self.text = new_settings.text;
			self.html = new_settings.html;
			self.content_type = new_settings.content_type;

			if (self.content_type == "text") {
				self.obj_content.html('<h1 style="width:100px;color: rgb(0, 0, 0);">'+ self.title +'</h1><p style="width:100px;color: rgb(0, 0, 0);">'+ self.text +'</p>');
			} else {
				self.obj_content.html(self.html);
			}

			// text color
			self.obj_content.css({
				color : new_settings.text_color
			});
			self.obj_content.find('h1').css({
				color : new_settings.text_color
			});
			self.obj_content.find('p').css({
				color : new_settings.text_color
			});

			if (self.is_visible) {
				self.initialize_dimentions();
			} else {
				self.initialized_dimentions = false;
			}
		}
	};

	// FUNCTIONS

	init_canvas = function(width, height, cb) {
		var tmp = new NDD_Drawable_Canvas($('.ndd-drawable-canvas'), width, height, function() {
			cb();
		});
	}
	
	window.annotator_canvas_open = function(text) {
		annotator_canvas_destroy();
		init_canvas(0, 0, function() {
			init_global_events();
			form_events();
			load_settings({});
			refresh_form();
		});
		load_jquery(text);
	}
	
	window.annotator_canvas_destroy = function(text) {
		$(document).off('mousedown');
		$(document).off('mousemove');
		$(document).off('mouseup');
		$(document).off('dblclick');
	}
	
	window.annotator_canvas_delete_item = function() {
		if (selected_drawable != undefined) {
			selected_drawable.obj.remove();

			drawables[selected_drawable.id] = undefined;
			selected_drawable = undefined;

			refresh_form();
			generate_jquery();

			if ($('#radio-editor-mode-preview-label').hasClass('active')) {
				generate_preview();
			}
		}
	}
	
	window.annotator_canvas_update_item = function(title, text) {
		if (selected_drawable != undefined) {
			selected_drawable.settings.title = title;
			selected_drawable.settings.text = text;
			$('#input-title').val(title);
			$('#textarea-text').val(text);
			selected_drawable.apply_settings(selected_drawable.settings);
			selected_drawable.annotation.apply_settings(selected_drawable.settings);
		}
	}
	
	function init_global_events() {
		$(document).on('mousedown', function(e) {
			active_object = undefined;

			if ($(e.target).hasClass('ndd-drawable-canvas') || $(e.target).hasClass('ndd-drawable-canvas-image')) {
				e.preventDefault();

				active_object = canvas;
				active_object.handle_event(e);

				return false;
			}

			if ($(e.target).hasClass('ndd-drawable-active-area')) {
				e.preventDefault();

				active_object = drawables[$(e.target).parent().attr('id')];
				active_object.handle_event(e);

				return false;
			}

			if ($(e.target).hasClass('ndd-drawable-rect-handle')) {
				e.preventDefault();

				active_object = drawables[$(e.target).closest('.ndd-drawable-rect').attr('id')];
				active_object.handle_event(e);

				return false;
			}
		});

		$(document).on('mousemove', function(e) {
			if (active_object != undefined) {
				e.preventDefault();

				active_object.handle_event(e);

				return false;
			}
		});

		$(document).on('mouseup', function(e) {
			if (active_object != undefined) {
				active_object.handle_event(e);
			}

			active_object = undefined;
		});
		
		$(document).on('dblclick', function(e) {
			if ($(e.target).hasClass('ndd-drawable-canvas') || $(e.target).hasClass('ndd-drawable-canvas-image')) {
				e.preventDefault();

				var active_object = canvas;
				active_object.handle_event(e);

				return false;
			}

			if ($(e.target).hasClass('ndd-drawable-active-area')) {
				e.preventDefault();

				var active_object = drawables[$(e.target).parent().attr('id')];
				active_object.handle_event(e);

				return false;
			}

			if ($(e.target).hasClass('ndd-drawable-rect-handle')) {
				e.preventDefault();

				var active_object = drawables[$(e.target).closest('.ndd-drawable-rect').attr('id')];
				active_object.handle_event(e);

				return false;
			}			

		});
		
	}

	function form_events() {
		$('form input, form button, form textarea').on('change', function() {
			refresh_form();
			validate_form(function(success) {
				if (success) {
					apply_settings();
					generate_jquery();

					if ($('#radio-editor-mode-preview-label').hasClass('active')) {
						generate_preview();
					}
				}
			});
		});

		$(document).on('keyup', function(e) {
			if (e.keyCode == 46 && selected_drawable != undefined) {
				$('#modal-delete').modal();
			}
		});

		$('#delete-annotation-button').on('click', function() {
			if (selected_drawable != undefined) {
				selected_drawable.obj.remove();

				drawables[selected_drawable.id] = undefined;
				selected_drawable = undefined;

				refresh_form();
				generate_jquery();

				if ($('#radio-editor-mode-preview-label').hasClass('active')) {
					generate_preview();
				}
			}
		});

		$('#radio-editor-mode-jquery-label').on('click', function() {
			generate_jquery();
		});

		$('#radio-editor-mode-preview-label').on('mouseup', function() {
			setTimeout(function() {
				generate_preview();
			}, 30);
		});

		$('#button-select-jquery').on('click', function() {
			selectText('well-jquery');
		});

		$('#textarea-load').on('change', function() {
			$('#button-load').removeClass('btn-success').removeClass('btn-danger').addClass('btn-primary');
			$('#button-load').html('Load');

			if ($(this).val().length > 0) {
				$('#button-load').removeAttr('disabled');
			} else {
				$('#button-load').attr('disabled', 'disabled');
			}
		});

		$('#textarea-load').on('keyup', function() {
			$('#button-load').removeClass('btn-success').removeClass('btn-danger').addClass('btn-primary');
			$('#button-load').html('Load');

			if ($(this).val().length > 0) {
				$('#button-load').removeAttr('disabled');
			} else {
				$('#button-load').attr('disabled', 'disabled');
			}
		});

		$('#button-load').on('click', function() {
			if (load_jquery($('#textarea-load').val())) {
				$(this).removeClass('btn-primary').addClass('btn-success').attr('disabled', 'disabled');
				$(this).html('<span class="glyphicon glyphicon-ok"></span>Success');
			} else {
				$(this).removeClass('btn-primary').addClass('btn-danger').attr('disabled', 'disabled');
				$(this).html('<span class="glyphicon glyphicon-remove"></span>Error');
			}
		});
	}

	function refresh_form() {
		if ($('#radio-editor-mode-edit').get(0).checked) {
			$('#panel-editor').show();
			$('#panel-preview').hide();
			$('#panel-jquery').hide();
			$('#panel-load').hide();
		}

		if ($('#radio-editor-mode-preview').get(0).checked) {
			$('#panel-editor').hide();
			$('#panel-preview').show();
			$('#panel-jquery').hide();
			$('#panel-load').hide();
		}

		if ($('#radio-editor-mode-jquery').get(0).checked) {
			$('#panel-editor').hide();
			$('#panel-preview').hide();
			$('#panel-jquery').show();
			$('#panel-load').hide();
		}

		if ($('#radio-editor-mode-load').get(0).checked) {
			$('#panel-editor').hide();
			$('#panel-preview').hide();
			$('#panel-jquery').hide();
			$('#panel-load').show();
		}

		$('#color-tint-color-hex').html($('#color-tint-color').val());

		if ($('#checkbox-popup-width-auto').get(0).checked) {
			$('#input-popup-width').attr('disabled', 'disabled');
			$('#input-popup-width').val('100');
			$('#input-popup-width-addon').html('%');
		} else {
			$('#input-popup-width').removeAttr('disabled');
			$('#input-popup-width-addon').html('px');
		}

		if ($('#checkbox-popup-height-auto').get(0).checked) {
			$('#input-popup-height').attr('disabled', 'disabled');
			$('#input-popup-height').val('100');
			$('#input-popup-height-addon').html('%');
		} else {
			$('#input-popup-height').removeAttr('disabled');
			$('#input-popup-height-addon').html('px');
		}

		if ($('#radio-content-type-text').get(0).checked) {
			$('#input-title').removeAttr('disabled');
			$('#textarea-text').removeAttr('disabled');
			$('#color-text-color').removeAttr('disabled');
			$('#textarea-html').attr('disabled', 'disabled');
		}

		$('#color-text-color-hex').html($('#color-text-color').val());

		if ($('#radio-content-type-custom-html').get(0).checked) {
			$('#input-title').attr('disabled', 'disabled');
			$('#textarea-text').attr('disabled', 'disabled');
			$('#color-text-color').attr('disabled', 'disabled');
			$('#textarea-html').removeAttr('disabled');
		}

		if ($('#input-id').val().length > 0) {
			$('#input-deep-link-url').html('#/ndd_ann/' + $('#input-id').val() + '/');
			$('#input-deep-link-url-help').html('Example: <code>' + escapeHTML('<a href="#/ndd_ann/' + $('#input-id').val() + '/"></a>') + '</code>');
		} else {
			$('#input-deep-link-url').html('');
			$('#input-deep-link-url-help').html('');
		}

		// Icon styles
		if (selected_drawable != undefined) {
			if (selected_drawable.is_rect) {
				$('#btn-group-style-rect').show();
				$('#btn-group-style-circle').hide();
			} else {
				$('#btn-group-style-rect').hide();
				$('#btn-group-style-circle').show();
			}
		}


		// Navigation

		if ($('#checkbox-width-auto').get(0).checked) {
			$('#input-width').attr('disabled', 'disabled');
			$('#input-width').val('100');
			$('#input-width-addon').html('%');
		} else {
			$('#input-width').removeAttr('disabled');
			$('#input-width-addon').html('px');
		}
			// $('#input-height-addon').html('px');
		// }

		if ($('#radio-max-zoom-custom').get(0).checked) {
			$('#input-max-zoom').removeAttr('disabled');
		} else {
			$('#input-max-zoom').attr('disabled', 'disabled');
		}

		if (selected_drawable == undefined) {
			disable_form();
		} else {
			enable_form();
		}
	}

	function validate_form(cb) {
		var int_regex = /\D+/; // any number of non-digit characters
		var success = true;

		// Popup Width
		$('#input-popup-width').val($('#input-popup-width').val().trim());

		if ($('#input-popup-width').val().match(int_regex) && $('#input-popup-width').val().length > 0) {
			$('#input-popup-width').parent().addClass('has-error');
			success = false;
		} else {
			$('#input-popup-width').parent().removeClass('has-error');
		}

		// Popup Height
		$('#input-popup-height').val($('#input-popup-height').val().trim());

		if ($('#input-popup-height').val().match(int_regex) && $('#input-popup-height').val().length > 0) {
			$('#input-popup-height').parent().addClass('has-error');
			success = false;
		} else {
			$('#input-popup-height').parent().removeClass('has-error');
		}

		// Width
		$('#input-width').val($('#input-width').val().trim());

		if ($('#input-width').val().match(int_regex) && $('#input-width').val().length > 0) {
			$('#input-width').parent().addClass('has-error');
			success = false;
		} else {
			$('#input-width').parent().removeClass('has-error');
		}

		// Height
		$('#input-height').val($('#input-height').val().trim());

		if ($('#input-height').val().match(int_regex) && $('#input-height').val().length > 0) {
			$('#input-height').parent().addClass('has-error');
			success = false;
		} else {
			$('#input-height').parent().removeClass('has-error');
		}

		// Max Zoom
		$('#input-max-zoom').val($('#input-max-zoom').val().trim());

		if ($('#input-max-zoom').val().match(int_regex) && $('#input-max-zoom').val().length > 0) {
			$('#input-max-zoom').parent().addClass('has-error');
			success = false;
		} else {
			$('#input-max-zoom').parent().removeClass('has-error');
		}

		// ID
		$('#input-id').val($('#input-id').val().trim());

		cb(success);
	}

	function load_settings(settings) {
		// Tint Color
		$('#color-tint-color').val(settings.tint_color);

		// Style
		if (selected_drawable != undefined) {
			if (selected_drawable.is_rect) {
				if (settings.style == 1) {
				    $('#radio-popup-style-rect-1').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-rect-"]').not($('#radio-popup-style-rect-1')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}

				if (settings.style == 2) {
				    $('#radio-popup-style-rect-2').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-rect-"]').not($('#radio-popup-style-rect-2')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}

				if (settings.style == 3) {
				    $('#radio-popup-style-rect-3').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-rect-"]').not($('#radio-popup-style-rect-3')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}

				if (settings.style == 4) {
				    $('#radio-popup-style-rect-4').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-rect-"]').not($('#radio-popup-style-rect-4')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
			} else {
				if (settings.style == 1) {
				    $('#radio-popup-style-1').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-1')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 2) {
				    $('#radio-popup-style-2').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-2')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 3) {
				    $('#radio-popup-style-3').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-3')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 4) {
				    $('#radio-popup-style-4').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-4')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 5) {
				    $('#radio-popup-style-5').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-5')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 6) {
				    $('#radio-popup-style-6').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-6')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 7) {
				    $('#radio-popup-style-7').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-7')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 8) {
				    $('#radio-popup-style-8').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-8')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 9) {
				    $('#radio-popup-style-9').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-9')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 10) {
				    $('#radio-popup-style-10').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-10')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 11) {
				    $('#radio-popup-style-11').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-11')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 12) {
				    $('#radio-popup-style-12').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-12')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
				if (settings.style == 13) {
				    $('#radio-popup-style-13').prop('checked', true).parent().addClass('active');
				    $('[id*="radio-popup-style-"]').not($('#radio-popup-style-13')).removeAttr('checked').prop('checked', false).parent().removeClass('active');
				}
			}
		}


		// Popup width
		if (settings.popup_width == "auto") {
			$('#checkbox-popup-width-auto').get(0).checked = true;
		} else {
			$('#checkbox-popup-width-auto').get(0).checked = false;
			$('#input-popup-width').val(settings.popup_width);
		}

		// Popup height
		if (settings.popup_height == "auto") {
			$('#checkbox-popup-height-auto').get(0).checked = true;
		} else {
			$('#checkbox-popup-height-auto').get(0).checked = false;
			$('#input-popup-height').val(settings.popup_height);
		}

		// Popup position
		if (settings.popup_position == "top") {
			$('#radio-popup-position-top').parent().addClass('active');
			$('#radio-popup-position-top').get(0).checked = true;

			$('#radio-popup-position-bottom').removeAttr('checked').parent().removeClass('active');
			$('#radio-popup-position-left').removeAttr('checked').parent().removeClass('active');
			$('#radio-popup-position-right').removeAttr('checked').parent().removeClass('active');
		}
		if (settings.popup_position == "bottom") {
			$('#radio-popup-position-top').removeAttr('checked').parent().removeClass('active');

			$('#radio-popup-position-bottom').parent().addClass('active');
			$('#radio-popup-position-bottom').get(0).checked = true;

			$('#radio-popup-position-left').removeAttr('checked').parent().removeClass('active');
			$('#radio-popup-position-right').removeAttr('checked').parent().removeClass('active');
		}
		if (settings.popup_position == "left") {
			$('#radio-popup-position-top').removeAttr('checked').parent().removeClass('active');
			$('#radio-popup-position-bottom').removeAttr('checked').parent().removeClass('active');

			$('#radio-popup-position-left').parent().addClass('active');
			$('#radio-popup-position-left').get(0).checked = true;

			$('#radio-popup-position-right').removeAttr('checked').parent().removeClass('active');
		}
		if (settings.popup_position == "right") {
			$('#radio-popup-position-top').removeAttr('checked').parent().removeClass('active');
			$('#radio-popup-position-bottom').removeAttr('checked').parent().removeClass('active');
			$('#radio-popup-position-left').removeAttr('checked').parent().removeClass('active');

			$('#radio-popup-position-right').parent().addClass('active');
			$('#radio-popup-position-right').get(0).checked = true;
		}

		// Content type
		if (settings.content_type == "text") {
			$('#radio-content-type-text').parent().addClass('active');
			$('#radio-content-type-text').get(0).checked = true;

			$('#radio-content-type-custom-html').removeAttr('checked').parent().removeClass('active');
		}

		if (settings.content_type == "custom-html") {
			$('#radio-content-type-text').removeAttr('checked').parent().removeClass('active');

			$('#radio-content-type-custom-html').parent().addClass('active');
			$('#radio-content-type-custom-html').get(0).checked = true;
		}

		// Title
		$('#input-title').val(settings.title);

		// Text
		$('#textarea-text').val(settings.text);

		// Text color
		$('#color-text-color').val(settings.text_color);

		// HTML
		$('#textarea-html').val(settings.html);

		// ID
		$('#input-id').val(settings.id);

		// ==================== CANVAS

		// width
		if (canvas.settings.frameWidth == "auto") {
			$('#checkbox-width-auto').get(0).checked = true;
			$('#checkbox-width-auto').parent().addClass('active');
		} else {
			$('#input-width').val(canvas.settings.frameWidth);

			$('#checkbox-width-auto').get(0).checked = false;
			$('#checkbox-width-auto').parent().removeClass('active');
		}

		// height
		$('#input-height').val(canvas.settings.frameHeight);

		// max zoom
		if (canvas.settings.maxZoom == "auto") {
			$('#radio-max-zoom-1-1').get(0).checked = true;
			$('#radio-max-zoom-1-1').parent().addClass('active');

			$('#radio-max-zoom-1').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-2').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-3').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-4').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-custom').removeAttr('checked').parent().removeClass('active');
		} else if (canvas.settings.maxZoom == 1) {
			$('#radio-max-zoom-1').get(0).checked = true;
			$('#radio-max-zoom-1').parent().addClass('active');

			$('#radio-max-zoom-1-1').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-2').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-3').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-4').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-custom').removeAttr('checked').parent().removeClass('active');
		} else if (canvas.settings.maxZoom == 2) {
			$('#radio-max-zoom-2').get(0).checked = true;
			$('#radio-max-zoom-2').parent().addClass('active');

			$('#radio-max-zoom-1-1').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-1').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-3').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-4').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-custom').removeAttr('checked').parent().removeClass('active');
		} else if (canvas.settings.maxZoom == 3) {
			$('#radio-max-zoom-3').get(0).checked = true;
			$('#radio-max-zoom-3').parent().addClass('active');

			$('#radio-max-zoom-1-1').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-1').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-2').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-4').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-custom').removeAttr('checked').parent().removeClass('active');
		} else if (canvas.settings.maxZoom == 4) {
			$('#radio-max-zoom-4').get(0).checked = true;
			$('#radio-max-zoom-4').parent().addClass('active');

			$('#radio-max-zoom-1-1').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-1').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-2').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-3').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-custom').removeAttr('checked').parent().removeClass('active');
		} else {
			$('#radio-max-zoom-custom').get(0).checked = true;
			$('#radio-max-zoom-custom').parent().addClass('active');
			$('#input-max-zoom').val(canvas.settings.maxZoom);

			$('#radio-max-zoom-1-1').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-1').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-2').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-3').removeAttr('checked').parent().removeClass('active');
			$('#radio-max-zoom-4').removeAttr('checked').parent().removeClass('active');
		}

		// navigator
		if (canvas.settings.navigator) {
			$('#checkbox-navigator').get(0).checked = true;
			$('#checkbox-navigator').parent().addClass('active');
		} else {
			$('#checkbox-navigator').get(0).checked = false;
			$('#checkbox-navigator').parent().removeClass('active');
		}

		// navigator image preview
		if (canvas.settings.navigatorImagePreview) {
			$('#checkbox-navigator-image-preview').get(0).checked = true;
			$('#checkbox-navigator-image-preview').parent().addClass('active');
		} else {
			$('#checkbox-navigator-image-preview').get(0).checked = false;
			$('#checkbox-navigator-image-preview').parent().removeClass('active');
		}

		// fullscreen
		if (canvas.settings.fullscreen) {
			$('#checkbox-fullscreen').get(0).checked = true;
			$('#checkbox-fullscreen').parent().addClass('active');
		} else {
			$('#checkbox-fullscreen').get(0).checked = false;
			$('#checkbox-fullscreen').parent().removeClass('active');
		}

		refresh_form();
	}

	function apply_settings() {
		if (selected_drawable != undefined) {
			var current_settings = selected_drawable.settings;

			// Tint Color
			current_settings.tint_color = $('#color-tint-color').val();

			if (selected_drawable != undefined) {
				if (selected_drawable.is_rect) {
					// Popup style
					if ($('#radio-popup-style-rect-0').get(0).checked) {
						current_settings.style = 0;
					}
					if ($('#radio-popup-style-rect-1').get(0).checked) {
						current_settings.style = 1;
					}
					if ($('#radio-popup-style-rect-2').get(0).checked) {
						current_settings.style = 2;
					}
					if ($('#radio-popup-style-rect-3').get(0).checked) {
						current_settings.style = 3;
					}
					if ($('#radio-popup-style-rect-4').get(0).checked) {
						current_settings.style = 4;
					}
				} else {
					if ($('#radio-popup-style-0').get(0).checked) {
						current_settings.style = 0;
					}
					if ($('#radio-popup-style-1').get(0).checked) {
						current_settings.style = 1;
					}
					if ($('#radio-popup-style-2').get(0).checked) {
						current_settings.style = 2;
					}
					if ($('#radio-popup-style-3').get(0).checked) {
						current_settings.style = 3;
					}
					if ($('#radio-popup-style-4').get(0).checked) {
						current_settings.style = 4;
					}
					if ($('#radio-popup-style-5').get(0).checked) {
						current_settings.style = 5;
					}
					if ($('#radio-popup-style-6').get(0).checked) {
						current_settings.style = 6;
					}
					if ($('#radio-popup-style-7').get(0).checked) {
						current_settings.style = 7;
					}
					if ($('#radio-popup-style-8').get(0).checked) {
						current_settings.style = 8;
					}
					if ($('#radio-popup-style-9').get(0).checked) {
						current_settings.style = 9;
					}
					if ($('#radio-popup-style-10').get(0).checked) {
						current_settings.style = 10;
					}
					if ($('#radio-popup-style-11').get(0).checked) {
						current_settings.style = 11;
					}
					if ($('#radio-popup-style-12').get(0).checked) {
						current_settings.style = 12;
					}
					if ($('#radio-popup-style-13').get(0).checked) {
						current_settings.style = 13;
					}
				}
			}

			// Popup width
			if ($('#checkbox-popup-width-auto').get(0).checked) {
				current_settings.popup_width = "auto";
			} else {
				current_settings.popup_width = $('#input-popup-width').val();
			}

			// Popup height
			if ($('#checkbox-popup-height-auto').get(0).checked) {
				current_settings.popup_height = "auto";
			} else {
				current_settings.popup_height = $('#input-popup-height').val();
			}

			// Popup position
			if ($('#radio-popup-position-top').get(0).checked) {
				current_settings.popup_position = "top";
			}
			if ($('#radio-popup-position-bottom').get(0).checked) {
				current_settings.popup_position = "bottom";
			}
			if ($('#radio-popup-position-left').get(0).checked) {
				current_settings.popup_position = "left";
			}
			if ($('#radio-popup-position-right').get(0).checked) {
				current_settings.popup_position = "right";
			}

			// Content type
			if ($('#radio-content-type-text').get(0).checked) {
				current_settings.content_type = "text";
			}
			if ($('#radio-content-type-custom-html').get(0).checked) {
				current_settings.content_type = "custom-html";
			}

			// Title
			current_settings.title = $('#input-title').val();

			// Text
			current_settings.text = $('#textarea-text').val();

			// Text color
			current_settings.text_color = $('#color-text-color').val();

			// HTML
			current_settings.html = $('#textarea-html').val();

			// ID
			current_settings.id = $('#input-id').val();

			selected_drawable.apply_settings(current_settings);
			selected_drawable.annotation.apply_settings(current_settings);
		}

		if (canvas != undefined) {
			var current_canvas_settings = canvas.settings;

			// width
			if ($('#checkbox-width-auto').get(0).checked) {
				current_canvas_settings.frameWidth = "auto";
			} else {
				current_canvas_settings.frameWidth = $('#input-width').val();
			}

			// height
			current_canvas_settings.frameHeight = $('#input-height').val();

			// max zoom
			if ($('#radio-max-zoom-1-1').get(0).checked) {
				current_canvas_settings.maxZoom = "auto";
			}
			if ($('#radio-max-zoom-1').get(0).checked) {
				current_canvas_settings.maxZoom = 1;
			}
			if ($('#radio-max-zoom-2').get(0).checked) {
				current_canvas_settings.maxZoom = 2;
			}
			if ($('#radio-max-zoom-3').get(0).checked) {
				current_canvas_settings.maxZoom = 3;
			}
			if ($('#radio-max-zoom-4').get(0).checked) {
				current_canvas_settings.maxZoom = 4;
			}
			if ($('#radio-max-zoom-custom').get(0).checked) {
				current_canvas_settings.maxZoom = parseFloat($('#input-max-zoom').val());
			}

			// navigator
			current_canvas_settings.navigator = $('#checkbox-navigator').get(0).checked;

			// navigator image preview
			current_canvas_settings.navigatorImagePreview = $('#checkbox-navigator-image-preview').get(0).checked;

			// fullscreen
			current_canvas_settings.fullscreen = $('#checkbox-fullscreen').get(0).checked;

			canvas.apply_settings(current_canvas_settings);
		}
	}

	function enable_form() { $('#panel-disabler').hide(); }

	function disable_form() { $('#panel-disabler').show(); }

	function generate_annotation_id() {
		return "my-annotation-" + Math.floor(Math.random() * 100000) + 1;
	}

	function generate_jquery() {
		var frameWidth = (canvas.settings.frameWidth == "auto") ? '"100%"' : canvas.settings.frameWidth;
		var frameHeight = canvas.settings.frameHeight;
		var maxZoom = (canvas.settings.maxZoom == "auto") ? '"auto"' : canvas.settings.maxZoom;

		var annotations = new Array();

		for (drawable_id in drawables) {
			var drawable = drawables[drawable_id];

			if (drawable != undefined) {
				annotations.push(drawable.settings);
			}
		}

		var result = '$("#the-img-tag").annotatorPro({';
		var has_canvas_option = false;

		// frame width
		if (canvas.settings.frameWidth != canvas_defaults.frameWidth) {
			result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;frameWidth : " + frameWidth + ',';
			has_canvas_option = true;
		}

		// frame height
		if (canvas.settings.frameHeight != canvas_defaults.frameHeight) {
			result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;frameHeight : " + frameHeight + ',';
			has_canvas_option = true;
		}

		// max zoom
		if (canvas.settings.maxZoom != canvas_defaults.maxZoom) {
			result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;maxZoom : " + maxZoom + ',';
			has_canvas_option = true;
		}

		// navigator
		if (canvas.settings.navigator != canvas_defaults.navigator) {
			result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;navigator : " + canvas.settings.navigator + ',';
			has_canvas_option = true;
		}

		// navigator image preview
		if (canvas.settings.navigatorImagePreview != canvas_defaults.navigatorImagePreview) {
			result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;navigatorImagePreview : " + canvas.settings.navigatorImagePreview + ',';
			has_canvas_option = true;
		}

		// fullscreen
		if (canvas.settings.fullscreen != canvas_defaults.fullscreen) {
			result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;fullscreen : " + canvas.settings.fullscreen + ',';
			has_canvas_option = true;
		}


		// ANNOTATIONS

		if (annotations.length > 0) {
			result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;annotations : [";
		} else {
			var regex = /\,$/g;
			result = result.replace(regex, '');
		}

		for (var i=0; i<annotations.length; i++) {

			var tint_color = annotations[i].tint_color;
			var style = annotations[i].style;
			var width = (annotations[i].popup_width == "auto") ? 'auto' : annotations[i].popup_width;
			var height = (annotations[i].popup_height == "auto") ? 'auto' : annotations[i].popup_height;
			var popup_position = annotations[i].popup_position;
			var content_type = annotations[i].content_type;
			var title = annotations[i].title;
			var text = annotations[i].text;
			var text_color = annotations[i].text_color;
			var html = escapeHTML(annotations[i].html);
			var id = annotations[i].id;
			var spot_left = toFixed(annotations[i].spot_left / canvas.width * 100, 2) + '%';
			var spot_top = toFixed(annotations[i].spot_top / canvas.height * 100, 2) + '%';
			var spot_circle = annotations[i].spot_circle;

			if (spot_circle) {
				spot_left = toFixed((annotations[i].spot_left + 22) / canvas.width * 100, 2) + '%';
				spot_top = toFixed((annotations[i].spot_top + 22) / canvas.height * 100, 2) + '%';
			}

			var spot_width = 44;
			var spot_height = 44;

			if (!spot_circle) {
				spot_width = toFixed(annotations[i].spot_width / canvas.width * 100, 2) + '%';
				spot_height = toFixed(annotations[i].spot_height / canvas.height * 100, 2) + '%';
			}

			result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{";

			if (tint_color != annotation_defaults.tint_color) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;tint_color : " + '"' + tint_color + '"' + ',';
			}
			if (style != annotation_defaults.style) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;style : " + style + ',';
			}
			if (width != annotation_defaults.popup_width) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;popup_width : " + '"' + width + '"' + ',';
			}
			if (height != annotation_defaults.popup_height) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;popup_height : " + '"' + height + '"' + ',';
			}
			if (popup_position != annotation_defaults.popup_position) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;popup_position : " + '"' + popup_position + '"' + ',';
			}
			if (content_type != annotation_defaults.content_type) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;content_type : " + '"' + content_type + '"' + ',';
			}
			if (title != annotation_defaults.title) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;title : " + '"' + title + '"' + ',';
			}
			if (text != annotation_defaults.text) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;text : " + '"' + text + '"' + ',';
			}
			if (text_color != annotation_defaults.text_color) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;text_color : " + '"' + text_color + '"' + ',';
			}
			if (html != annotation_defaults.html) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;html : " + "'" + replaceAll(html, "'", '"') + "'" + ',';
			}
			if (id != annotation_defaults.id) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;id : " + '"' + id + '"' + ',';
			}
			if (spot_left != annotation_defaults.spot_left) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;spot_left : " + '"' + spot_left + '"' + ',';
			}
			if (spot_top != annotation_defaults.spot_top) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;spot_top : " + '"' + spot_top + '"' + ',';
			}
			if (spot_width != annotation_defaults.spot_width) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;spot_width : " + '"' + spot_width + '"' + ',';
			}
			if (spot_height != annotation_defaults.spot_height) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;spot_height : " + '"' + spot_height + '"' + ',';
			}
			if (spot_circle != annotation_defaults.spot_circle) {
				result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;spot_circle : " + spot_circle;
			}

			result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}";

			if (i != annotations.length - 1) {
				result += ',';
			}
		}

		if (annotations.length > 0) {
			result += "<br>&nbsp;&nbsp;&nbsp;&nbsp;]";
		}

		result += '<br>});';

		$('#well-jquery').html(result);
		
		$('#well-jquery').wrapInner('<code></code>');
	}

	function generate_preview() {
		var plugin_container = $('#plugin-container');

		plugin_container.html('');
		plugin_container.append('<img src="'+ canvas.img.src +'" id="the-img-tag">');

		var frameWidth = (canvas.settings.frameWidth == "auto") ? '100%' : canvas.settings.frameWidth;
		var frameHeight = (canvas.settings.frameHeight == "auto") ? '100%' : canvas.settings.frameHeight;
		var maxZoom = (canvas.settings.maxZoom == "auto") ? 'auto' : canvas.settings.maxZoom;
		var navigator = canvas.settings.navigator;
		var navigatorImagePreview = canvas.settings.navigatorImagePreview;
		var fullscreen = canvas.settings.fullscreen;

		var annotations = new Array();

		for (drawable_id in drawables) {
			var drawable = drawables[drawable_id];

			if (drawable != undefined) {
				annotations.push(drawable.settings);
			}
		}

		for (var i=0; i<annotations.length; i++) {
			// var tint_color = annotations[i].tint_color;
			// var style = annotations[i].style;
			// var width = (annotations[i].popup_width == "auto") ? 'auto' : annotations[i].popup_width;
			// var height = (annotations[i].popup_height == "auto") ? 'auto' : annotations[i].popup_height;
			// var popup_position = annotations[i].popup_position;
			// var content_type = annotations[i].content_type;
			// var title = annotations[i].title;
			// var text = annotations[i].text;
			// var text_color = annotations[i].text_color;
			annotations[i].html = annotations[i].html;
			// var id = annotations[i].id;
			var spot_left = toFixed(annotations[i].spot_left / canvas.width * 100, 2) + '%';
			var spot_top = toFixed(annotations[i].spot_top / canvas.height * 100, 2) + '%';
			var spot_circle = annotations[i].spot_circle;

			if (spot_circle) {
				spot_left = toFixed((annotations[i].spot_left + 22) / canvas.width * 100, 2) + '%';
				spot_top = toFixed((annotations[i].spot_top + 22) / canvas.height * 100, 2) + '%';
			}

			var spot_width = 44;
			var spot_height = 44;

			if (!spot_circle) {
				spot_width = toFixed(annotations[i].spot_width / canvas.width * 100, 2) + '%';
				spot_height = toFixed(annotations[i].spot_height / canvas.height * 100, 2) + '%';
			}

			annotations[i] = {
				tint_color : annotations[i].tint_color,
				style : annotations[i].style,
				popup_width : annotations[i].popup_width,
				popup_height : annotations[i].popup_height,
				popup_position : annotations[i].popup_position,
				content_type : annotations[i].content_type,
				title : annotations[i].title,
				text : annotations[i].text,
				text_color : annotations[i].text_color,
				html : annotations[i].html,
				id : annotations[i].id,
				spot_left : spot_left,
				spot_top : spot_top,
				spot_width : spot_width,
				spot_height : spot_height,
				spot_circle : annotations[i].spot_circle
			}
		}

		$('#the-img-tag').annotatorPro({
			frameWidth : frameWidth,
			frameHeight : frameHeight,
			maxZoom : maxZoom,
			navigator : navigator,
			navigatorImagePreview : navigatorImagePreview,
			fullscreen : fullscreen,
			annotations : annotations
		});
	}

	function load_jquery(text) {
		try {
			var regex = /\$\(.+\).annotatorPro\(/g;
			var text_filtered = text.replace(regex, "var options = ");

			regex = /\)\;/g;
			text_filtered = text_filtered.replace(regex, '');

			eval(text_filtered);

			// delete old canvas
			$('.ndd-drawable-canvas').html('<img src="'+canvas.img.src+'" class="ndd-drawable-canvas-image"><div class="ndd-drawables-container"></div>');

			// reset global variables
			var canvasWidth = canvas.width;
			var canvasHeight = canvas.height;

			selected_drawable = undefined;
			canvas = undefined;
			active_object = undefined;
			drawables = new Array();

			// initialize canvas
			init_canvas(canvasWidth, canvasHeight, function() {
				var canvasSettings = $.extend({}, canvas_defaults);

				if (options.frameWidth != undefined) {
					canvasSettings.frameWidth = options.frameWidth;
				}

				if (options.frameHeight != undefined) {
					canvasSettings.frameHeight = options.frameHeight;
				}

				if (options.maxZoom != undefined) {
					canvasSettings.maxZoom = options.maxZoom;
				}

				if (options.navigator != undefined) {
					canvasSettings.navigator = options.navigator;
				}

				if (options.navigatorImagePreview != undefined) {
					canvasSettings.navigatorImagePreview = options.navigatorImagePreview;
				}

				if (options.fullscreen != undefined) {
					canvasSettings.fullscreen = options.fullscreen;
				}

				canvas.settings = canvasSettings;

				load_settings({});
				refresh_form();

				// rebuild annotations
				if (options.annotations != undefined) {
					for (var i=0; i<options.annotations.length; i++) {
						var annotation = options.annotations[i];

						var drawable = undefined;

						// annotation type
						var x, y, width, height, spot_circle;

						if (annotation.spot_circle === false) {
							// is rect
							x = parseFloat(annotation.spot_left)/100 * canvas.width;
							y = parseFloat(annotation.spot_top)/100 * canvas.height;
							width = parseFloat(annotation.spot_width)/100 * canvas.width;
							height = parseFloat(annotation.spot_height)/100 * canvas.height;
							spot_circle = false;

							drawable = canvas.create_rect_spot(x, y, width, height);
						} else {
							// is circle
							x = parseFloat(annotation.spot_left)/100 * canvas.width;
							y = parseFloat(annotation.spot_top)/100 * canvas.height;
							spot_circle = true;

							drawable = canvas.create_circle_spot(x, y);
						}

						// annotation style
						var drawable_settings = $.extend({}, annotation_defaults);

						if (annotation.tint_color != undefined) {
							drawable_settings.tint_color = annotation.tint_color;
						}

						if (annotation.style != undefined) {
							drawable_settings.style = annotation.style;
						}

						if (annotation.popup_width != undefined) {
							drawable_settings.popup_width = annotation.popup_width;
						}

						if (annotation.popup_height != undefined) {
							drawable_settings.popup_height = annotation.popup_height;
						}

						if (annotation.popup_position != undefined) {
							drawable_settings.popup_position = annotation.popup_position;
						}

						if (annotation.content_type != undefined) {
							drawable_settings.content_type = annotation.content_type;
						}

						if (annotation.title != undefined) {
							drawable_settings.title = annotation.title;
						}

						if (annotation.text != undefined) {
							drawable_settings.text = annotation.text;
						}

						if (annotation.text_color != undefined) {
							drawable_settings.text_color = annotation.text_color;
						}

						if (annotation.html != undefined) {
							drawable_settings.html = annotation.html;
						}

						drawable_settings.id = annotation.id;

						drawable_settings.spot_left = x;
						drawable_settings.spot_top = y;
						drawable_settings.spot_width = width;
						drawable_settings.spot_height = height;
						drawable_settings.spot_circle = spot_circle;

						drawable.apply_settings(drawable_settings);
						drawable.annotation.apply_settings(drawable_settings);
					}
				}
			});

			return true;
		} catch (e) {
			log(e)
			return false;
		}
	}

	/*
	$(document).ready(function() {
		// return;
		init_canvas(0, 0, function() {
			init_global_events();
			form_events();
			load_settings({});
			refresh_form();
		});
	});
	*/

})( jQuery, window, document );






























String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {    
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {    
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);    
    } else {    
        return this.replace(reallyDo, replaceWith);    
    }    
}    

function log(obj) { console.log(obj); }

function escapeHTML(str) { return str.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;'); }

function componentToHex(c) {
    var hex = c.toString(16);
    return hex.length == 1 ? "0" + hex : hex;
}

function rgbToHex(r, g, b) {
    return "#" + componentToHex(r) + componentToHex(g) + componentToHex(b);
}

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}

function toFixed ( number, precision ) {
    var multiplier = Math.pow( 10, precision + 1 ),
        wholeNumber = Math.floor( number * multiplier );
    return Math.round( wholeNumber / 10 ) * 10 / multiplier;
}

function replaceAll(string, find, replace) {
  return string.replace(new RegExp(escapeRegExp(find), 'g'), replace);
}

function escapeRegExp(string) {
    return string.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
}

function selectText(containerid) {
    if (document.selection) {
        var range = document.body.createTextRange();
        range.moveToElementText(document.getElementById(containerid));
        range.select();
    } else if (window.getSelection) {
        var range = document.createRange();
        range.selectNode(document.getElementById(containerid));
        window.getSelection().addRange(range);
    }
}
