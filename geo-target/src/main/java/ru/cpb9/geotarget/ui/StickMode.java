package ru.cpb9.geotarget.ui;

/**
 * @author Artem Shein
 */
public enum StickMode {
    LEFT_TOP {
        @Override
        public boolean isMode(Widget widget) {
            return (widget.getLayoutX() < widget.getStickingWidth()) && (widget.getLayoutY() < widget.getStickingWidth());
        }

        @Override
        public void update(Widget widget) {
            widget.setRotate(0.);
            widget.setLayoutX(0.);
            widget.setLayoutY(0.);
        }
    },

    LEFT_BOTTOM {
        @Override
        public boolean isMode(Widget widget) {
            return (widget.getLayoutX() < widget.getStickingWidth()) && (widget.getParent().getLayoutBounds().getHeight() - widget.getLayoutY() - widget.getHeight() < widget.getStickingWidth());
        }

        @Override
        public void update(Widget widget) {
            widget.setRotate(0.);
            widget.setLayoutX(0.);
            if (widget.getPrefHeight() == -1) {
                widget.setLayoutY(widget.getParent().getLayoutBounds().getHeight() - widget.getHeight());
            } else {
                widget.setLayoutY(widget.getParent().getLayoutBounds().getHeight() - widget.getPrefHeight());
            }
        }
    },

    RIGHT_TOP {
        @Override
        public boolean isMode(Widget widget) {
            return (widget.getParent().getLayoutBounds().getWidth() - widget.getLayoutX() - widget.getWidth() < widget.getStickingWidth()) && (widget.getLayoutY() < widget.getStickingWidth());
        }

        @Override
        public void update(Widget widget) {
            widget.setRotate(0.);
            widget.setLayoutX(widget.getParent().getLayoutBounds().getWidth() - widget.getWidth());
            widget.setLayoutY(0.);
        }
    },

    RIGHT_BOTTOM {
        @Override
        public boolean isMode(Widget widget) {
            return (widget.getParent().getLayoutBounds().getWidth() - widget.getLayoutX() - widget.getWidth() < widget.getStickingWidth()) && (widget.getParent().getLayoutBounds().getHeight() - widget.getLayoutY() - widget.getHeight() < widget.getStickingWidth());
        }

        @Override
        public void update(Widget widget) {
            widget.setRotate(0.);
            widget.setLayoutX(widget.getParent().getLayoutBounds().getWidth() - widget.getWidth());
            if (widget.getPrefHeight() == -1) {
                widget.setLayoutY(widget.getParent().getLayoutBounds().getHeight() - widget.getHeight());
            } else {
                widget.setLayoutY(widget.getParent().getLayoutBounds().getHeight() - widget.getPrefHeight());
            }
        }
    },

    LEFT {
        @Override
        public boolean isMode(
                Widget widget) {
            return widget.getLayoutX() < widget.getStickingWidth();
        }

        @Override
        public void update(Widget widget) {
            widget.setRotate(widget.isMinimized() ? 90. : 0.);
            widget.setLayoutX(widget.isMinimized() ? -widget.getWidth() / 2 + widget.getPrefHeight() / 2 : 0);
            if (widget.isMinimized() && widget.getLayoutY() < widget.getWidth() / 2 - widget.getHeight() / 2) {
                widget.setLayoutY(widget.getWidth() / 2 - widget.getHeight() / 2);
            } else if (widget.isMinimized() && widget.getLayoutY() > widget.getParent().getLayoutBounds().getHeight() - widget.getWidth() / 2 - widget.getHeight() / 2) {
                widget.setLayoutY(widget.getParent().getLayoutBounds().getHeight() - widget.getWidth() / 2 - widget.getHeight() / 2);
            }
        }
    },

    RIGHT {
        @Override
        public boolean isMode(
                Widget widget) {
            return widget.getParent().getLayoutBounds().getWidth() - widget.getLayoutX() - widget.getWidth() < widget.getStickingWidth();
        }

        @Override
        public void update(Widget widget) {
            widget.setRotate(widget.isMinimized() ? -90. : 0);
            widget.setLayoutX(widget.getParent().getLayoutBounds().getWidth() - (widget.isMinimized() ? widget.getWidth() / 2 + widget.getPrefHeight() / 2 : widget.getWidth()));
            if (widget.isMinimized() && widget.getLayoutY() < widget.getWidth() / 2 - widget.getHeight() / 2) {
                widget.setLayoutY(widget.getWidth() / 2 - widget.getHeight() / 2);
            } else if (widget.isMinimized() && widget.getLayoutY() > widget.getParent().getLayoutBounds().getHeight() - widget.getWidth() / 2 - widget.getHeight() / 2) {
                widget.setLayoutY(widget.getParent().getLayoutBounds().getHeight() - widget.getWidth() / 2 - widget.getHeight() / 2);
            }
        }
    },

    TOP {
        @Override
        public boolean isMode(Widget widget) {
            return widget.getLayoutY() < widget.getStickingWidth();
        }

        @Override
        public void update(Widget widget) {
            widget.setRotate(0.);
            widget.setLayoutY(0.);
        }
    },

    BOTTOM {
        @Override
        public boolean isMode(Widget widget) {
            return widget.getParent().getLayoutBounds().getHeight() - widget.getLayoutY() - widget.getHeight() < widget.getStickingWidth();
        }

        @Override
        public void update(Widget widget) {
            widget.setRotate(0.);
            if (widget.getPrefHeight() == -1) {
                widget.setLayoutY(widget.getParent().getLayoutBounds().getHeight() - widget.getHeight());
            } else {
                widget.setLayoutY(widget.getParent().getLayoutBounds().getHeight() - widget.getPrefHeight());
            }
        }
    },

    NONE {
        @Override
        public boolean isMode(Widget widget) {
            return true;
        }

        @Override
        public void update(Widget widget) {
            widget.setRotate(0.);
        }
    };

    public abstract boolean isMode(Widget widget);

    public abstract void update(Widget widget);

    public static StickMode findMode(Widget widget) {
        for (StickMode mode : StickMode.values()) {
            if (mode.isMode(widget)) {
                return mode;
            }
        }
        return NONE;
    }
}