/*
 * Copyright (C) 2014, 2017. Miroslav Wengner, Marcus Hirt
 * This ButtonUnit.java  is part of robo4j.
 * module: robo4j-units-lego
 *
 * robo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * robo4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with robo4j .  If not, see <http://www.gnu.org/licenses/>.
 */

package com.robo4j.units.lego;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.robo4j.core.ConfigurationException;
import com.robo4j.core.LifecycleState;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboReference;
import com.robo4j.core.RoboUnit;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.hw.lego.enums.ButtonTypeEnum;
import com.robo4j.hw.lego.util.BrickUtils;
import com.robo4j.units.lego.brick.ButtonListener;
import com.robo4j.units.lego.brick.PlateButtonEnum;
import com.robo4j.units.lego.brick.PlateButtonI;
import com.robo4j.units.lego.enums.LegoPlatformMessageTypeEnum;

import lejos.hardware.KeyListener;

/**
 * Lego Mindstorm Brick button plate
 *
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class BrickButtonsUnit extends RoboUnit<String> {
	private static final int COLOR_GREEN = 1;
	private String target;
	private List<ButtonTypeEnum> availablePlateButtons = Arrays.asList(ButtonTypeEnum.LEFT, ButtonTypeEnum.RIGHT,
			ButtonTypeEnum.UP, ButtonTypeEnum.DOWN, ButtonTypeEnum.ENTER);
	private Map<PlateButtonEnum, ButtonTypeEnum> buttons;

	public BrickButtonsUnit(RoboContext context, String id) {
		super(String.class, context, id);
	}

	/**
	 *
	 * @param configuration
	 *            the {@link Configuration} provided.
	 * @throws ConfigurationException
	 */
	@Override
	protected void onInitialization(Configuration configuration) throws ConfigurationException {
		setState(LifecycleState.UNINITIALIZED);
		target = configuration.getString("target", null);
		if (target == null) {
			throw ConfigurationException.createMissingConfigNameException("target");
		}

		//@formatter:off
        /* initiate button plate */
        buttons = availablePlateButtons.stream()
                .map(lb -> {
                    String bPropName = configuration.getString(BrickUtils.getButton(lb.getName()), null);
                    PlateButtonEnum plateB = PlateButtonEnum.getByName(bPropName);
                    return (bPropName == null || plateB == null) ? null : mapButton().apply(plateB, lb);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(PlateButtonI::getKey, PlateButtonI::getValue));
        //@formatter:on
		setState(LifecycleState.INITIALIZED);
	}

	@Override
	public void start() {
		setState(LifecycleState.STARTING);
		final RoboReference<LegoPlatformMessageTypeEnum> targetRef = getContext().getReference(target);

		//@formatter:off
        buttons.entrySet().stream()
                .forEach(e ->
                    e.getValue().getType()
							.addKeyListener((KeyListener) new ButtonListener(targetRef, e.getKey(), COLOR_GREEN))
                );
        //@formatter:on
		setState(LifecycleState.STARTED);
	}

	// Private Methods
	private BiFunction<PlateButtonEnum, ButtonTypeEnum, PlateButtonI> mapButton() {
		return (PlateButtonEnum plateButton, ButtonTypeEnum legoButton) -> new PlateButtonI() {
			@Override
			public PlateButtonEnum getKey() {
				return plateButton;
			}

			@Override
			public ButtonTypeEnum getValue() {
				return legoButton;
			}
		};
	}
}
